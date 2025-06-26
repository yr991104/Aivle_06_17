package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestPublicationDenied extends AbstractEvent {

    private Long id;

    public RequestPublicationDenied(EBook aggregate) {
        super(aggregate);
    }

    public RequestPublicationDenied() {
        super();
    }
}
//>>> DDD / Domain Event
