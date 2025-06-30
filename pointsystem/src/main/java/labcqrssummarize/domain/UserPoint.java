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
import lombok.Data;

/**
 * 사용자 포인트를 관리하는 엔티티 (Aggregate Root)
 * - 포인트 지급/차감
 * - 포인트 히스토리 관리
 */
@Entity
@Table(name = "UserPoint_table")
@Data
public class UserPoint {

    @Id
    private String userId; // 사용자 ID

    private Integer point; // 현재 보유 포인트

    private PointHistory pointHistory; // 최근 포인트 변동 내역

    /**
     * JPA Persist 후 포인트 지급 이벤트 발행
     */
    @PostPersist
    public void onPostPersist() {
        GivenPoint givenPoint = new GivenPoint(this);
        givenPoint.publishAfterCommit();
    }

    /**
     * JPA Update 전 포인트 차감 이벤트 발행
     */
    @PreUpdate
    public void onPreUpdate() {
        ReducedPoint reducedPoint = new ReducedPoint(this);
        reducedPoint.publishAfterCommit();
    }

    /**
     * UserPointRepository 빈을 반환 (정적 메서드에서 사용)
     */
    public static UserPointRepository repository() {
        UserPointRepository userPointRepository = PointsystemApplication.applicationContext.getBean(
            UserPointRepository.class
        );
        return userPointRepository;
    }

    /**
     * 포인트 지급 로직
     * @param command 지급 명령
     */
    public static void givePoint(GivePointCommand command) {
        UserPoint userPoint = repository().findById(command.getUserId()).orElse(null);
        
        if (userPoint == null) {
            userPoint = new UserPoint();
            userPoint.setUserId(command.getUserId());
            userPoint.setPoint(0);
        }
        
        userPoint.setPoint(userPoint.getPoint() + command.getPoint());
        
        // 포인트 히스토리 생성
        PointHistory pointHistory = new PointHistory();
        pointHistory.setUserId(command.getUserId());
        pointHistory.setPoint(command.getPoint());
        pointHistory.setType("GIVEN");
        pointHistory.setDescription(command.getDescription());
        pointHistory.setCreatedAt(new Date());
        
        userPoint.setPointHistory(pointHistory);
        
        repository().save(userPoint);
    }

    /**
     * 포인트 차감 로직
     * @param command 차감 명령
     */
    public static void reducePoint(ReducePointCommand command) {
        UserPoint userPoint = repository().findById(command.getUserId()).orElse(null);
        
        if (userPoint == null) {
            throw new RuntimeException("User not found: " + command.getUserId());
        }
        
        if (userPoint.getPoint() < command.getPoint()) {
            throw new RuntimeException("Insufficient points for user: " + command.getUserId());
        }
        
        userPoint.setPoint(userPoint.getPoint() - command.getPoint());
        
        // 포인트 히스토리 생성
        PointHistory pointHistory = new PointHistory();
        pointHistory.setUserId(command.getUserId());
        pointHistory.setPoint(command.getPoint());
        pointHistory.setType("REDUCED");
        pointHistory.setDescription(command.getDescription());
        pointHistory.setCreatedAt(new Date());
        
        userPoint.setPointHistory(pointHistory);
        
        repository().save(userPoint);
    }
}
