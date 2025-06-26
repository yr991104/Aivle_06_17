package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestAsAuthorDenied extends AbstractEvent {

    private Long id;

    public RequestAsAuthorDenied(Author aggregate) {
        super(aggregate);
    }

    public RequestAsAuthorDenied() {
        super();
    }
}
//>>> DDD / Domain Event
