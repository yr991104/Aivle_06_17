package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestAsAuthorApproved extends AbstractEvent {

    private Long id;

    public RequestAsAuthorApproved(Author aggregate) {
        super(aggregate);
    }

    public RequestAsAuthorApproved() {
        super();
    }
}
//>>> DDD / Domain Event
