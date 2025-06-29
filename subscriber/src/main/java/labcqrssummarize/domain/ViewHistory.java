package labcqrssummarize.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class ViewHistory {

    private String historyId;
    private String ebookId;
    private LocalDateTime viewedAt;
    private Integer usedPoint;

    protected ViewHistory() {}

    public ViewHistory(
        String historyId,
        String ebookId,
        LocalDateTime viewedAt,
        Integer usedPoint
    ) {
        this.historyId = historyId;
        this.ebookId = ebookId;
        this.viewedAt = viewedAt;
        this.usedPoint = usedPoint;
    }
}
