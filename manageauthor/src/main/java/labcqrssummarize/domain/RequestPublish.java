package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestPublish extends AbstractEvent {

    private Long ebookId;
    private String authorId;
    private String title;
    private Boolean isApproved;

    public RequestPublish(Ebook aggregate) {
        super(aggregate);
        this.ebookId = aggregate.getId();
        this.authorId = aggregate.getAuthorId();
        this.title = aggregate.getTitle();
    }

    public RequestPublish() {
        super();
    }
}
//>>> DDD / Domain Event
