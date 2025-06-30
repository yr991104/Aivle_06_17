package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RegisteredAuthor extends AbstractEvent {

    private String authorId;
    private Boolean isApproved;
    private String userId;

    public RegisteredAuthor(Author aggregate) {
        super(aggregate);
        this.authorId = aggregate.getAuthorId();
        this.isApproved = aggregate.getIsApproved();
        this.userId = aggregate.getUserId();
    }

    public RegisteredAuthor() {
        super();
    }
}
//>>> DDD / Domain Event
