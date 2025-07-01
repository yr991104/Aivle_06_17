package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.PointsystemApplication;
import labcqrssummarize.domain.GivenPoint;
import labcqrssummarize.domain.ReducedPoint;
import labcqrssummarize.infra.SubscriberService;
import lombok.Data;

@Entity
@Table(name = "UserPoint_table")
@Data
//<<< DDD / Aggregate Root
public class UserPoint {

    @Id
    private String userId;

    private Integer point;

    @PostPersist
    public void onPostPersist() {
        GivenPoint givenPoint = new GivenPoint(this);
        givenPoint.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        ReducedPoint reducedPoint = new ReducedPoint(this);
        reducedPoint.publishAfterCommit();
    }

    public static UserPointRepository repository() {
        UserPointRepository userPointRepository = PointsystemApplication.applicationContext.getBean(
            UserPointRepository.class
        );
        return userPointRepository;
    }

    public static SubscriberService subscriberService() {
        SubscriberService subscriberService = PointsystemApplication.applicationContext.getBean(
            SubscriberService.class
        );
        return subscriberService;
    }

    //<<< Clean Arch / Port Method
    public static void givePoint(GivePointCommand givePointCommand) {
        //implement business logic here:
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(givePointCommand.getUserId());
        userPoint.setPoint(givePointCommand.getPoint());
        
        repository().save(userPoint);
    }

    public static PointDeductionResponse deductPoint(PointDeductionRequest request) {
        PointDeductionResponse response = new PointDeductionResponse();
        response.setUserId(request.getUserId());
        response.setEbookId(request.getEbookId());

        try {
            // 1. GET 요청으로 subscriber 서비스에 구독 상태 확인
            SubscriberStatusResponse statusResponse = subscriberService().getSubscriberStatus(request.getUserId());

            if (!statusResponse.isSuccess()) {
                response.setSuccess(false);
                response.setMessage("구독자 상태 확인 실패: " + statusResponse.getMessage());
                return response;
            }

            // 2. 구독 상태에 따른 포인트 차감
            if (statusResponse.getSubscriptionStatus() == SubscriptionStatus.SUBSCRIBED) {
                // 구독 중이면 0포인트 차감
                response.setDeductedPoint(0);
                response.setMessage("구독 중인 사용자입니다. 포인트가 차감되지 않습니다.");
            } else {
                // 구독 중이 아니면 price만큼 차감
                UserPoint userPoint = repository().findById(request.getUserId()).orElse(null);

                if (userPoint == null) {
                    response.setSuccess(false);
                    response.setMessage("포인트 정보를 찾을 수 없습니다.");
                    return response;
                }

                if (userPoint.getPoint() < request.getPrice()) {
                    response.setSuccess(false);
                    response.setMessage("보유 포인트가 부족합니다. 필요: " + request.getPrice() + ", 보유: " + userPoint.getPoint());
                    return response;
                }

                // 포인트 차감
                userPoint.setPoint(userPoint.getPoint() - request.getPrice());
                repository().save(userPoint);

                response.setDeductedPoint(request.getPrice());
                response.setRemainingPoint(userPoint.getPoint());
                response.setMessage("포인트가 성공적으로 차감되었습니다.");
            }

            response.setSuccess(true);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("포인트 차감 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }

    public static void reducePoint(ReducePointCommand reducePointCommand) {
        //implement business logic here:
        repository().findById(reducePointCommand.getUserId()).ifPresent(userPoint -> {
            userPoint.setPoint(userPoint.getPoint() - reducePointCommand.getPoint());
            repository().save(userPoint);
        });
    }
    //>>> Clean Arch / Port Method
}

//>>> DDD / Aggregate Root
