package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestAuthorApproved extends AbstractEvent {

    private String authorId;
    private Boolean isApproved;
    private String userId;

    public RequestAuthorApproved(Author aggregate) {
        super(aggregate);
    }

    public RequestAuthorApproved() {
        super();
    }
}
//>>> DDD / Domain Event
