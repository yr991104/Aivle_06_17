package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestPublicationApproved extends AbstractEvent {

    private Long id;

    public RequestPublicationApproved(EBook aggregate) {
        super(aggregate);
    }

    public RequestPublicationApproved() {
        super();
    }
}
//>>> DDD / Domain Event
