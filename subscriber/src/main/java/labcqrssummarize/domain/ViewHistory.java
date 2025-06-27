package labcqrssummarize.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String historyId;

    private String ebookId;

    private LocalDateTime viewedAt;

    private Integer usedPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    public ViewHistory() {}

    public ViewHistory(
        String historyId,
        String ebookId,
        LocalDateTime viewedAt,
        Integer usedPoint,
        Subscriber subscriber
    ) {
        this.historyId = historyId;
        this.ebookId = ebookId;
        this.viewedAt = viewedAt;
        this.usedPoint = usedPoint;
        this.subscriber = subscriber;
    }
}
