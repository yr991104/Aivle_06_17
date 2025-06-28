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

@Entity
@Table(name = "UserPoint_table")
@Data
//<<< DDD / Aggregate Root
public class UserPoint {

    @Id
    private String userId;

    private Integer point;

    private PointHistory pointHistory;

    @PostPersist
    public void onPostPersist() {
        ReducedPoint reducedPoint = new ReducedPoint(this);
        reducedPoint.publishAfterCommit();

        GivenPoint givenPoint = new GivenPoint(this);
        givenPoint.publishAfterCommit();
    }

    public static UserPointRepository repository() {
        UserPointRepository userPointRepository = PointsystemApplication.applicationContext.getBean(
            UserPointRepository.class
        );
        return userPointRepository;
    }

    //<<< Clean Arch / Port Method
    public static void givePoint(GivePointCommand givePointCommand) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(givePointCommand.getUserId());
        userPoint.setPoint(givePointCommand.getPoint());
        
        // Create point history
        PointHistory pointHistory = new PointHistory();
        pointHistory.setChangeAmount("+" + givePointCommand.getPoint().toString());
        pointHistory.setDescription(givePointCommand.getDescription());
        pointHistory.setChangedAt(LocalDate.now().toString());
        userPoint.setPointHistory(pointHistory);
        
        repository().save(userPoint);
    }

    public static void reducePoint(ReducePointCommand reducePointCommand) {
        UserPoint userPoint = repository().findById(reducePointCommand.getUserId()).orElse(null);
        if (userPoint != null) {
            userPoint.setPoint(userPoint.getPoint() - reducePointCommand.getPoint());
            
            // Create point history
            PointHistory pointHistory = new PointHistory();
            pointHistory.setChangeAmount("-" + reducePointCommand.getPoint().toString());
            pointHistory.setDescription(reducePointCommand.getDescription());
            pointHistory.setChangedAt(LocalDate.now().toString());
            userPoint.setPointHistory(pointHistory);
            
            repository().save(userPoint);
        }
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
