package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestContentApporved extends AbstractEvent {

    private Long id;

    public RequestContentApporved(EBook aggregate) {
        super(aggregate);
    }

    public RequestContentApporved() {
        super();
    }
}
//>>> DDD / Domain Event
