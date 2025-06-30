package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class WrittenContent extends AbstractEvent {

    private Long id;
    private String authorId;
    private Boolean isApproved;

    public WrittenContent(Author aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.authorId = aggregate.getauthorId();
        this.isApproved = aggregate.getisApproved();
    }

    public WrittenContent() {
        super();
    }
}
//>>> DDD / Domain Event
