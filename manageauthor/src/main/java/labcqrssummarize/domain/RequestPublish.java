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

    private Long id;
    private String authorId;
    private Boolean isApproved;

    public RequestPublish(Author aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.authorId = aggregate.getauthorId();
        this.isApproved = aggregate.getisApproved();
    }

    public RequestPublish() {
        super();
    }
}
//>>> DDD / Domain Event
