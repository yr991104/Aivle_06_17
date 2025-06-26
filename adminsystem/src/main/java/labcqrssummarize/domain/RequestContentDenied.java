package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestContentDenied extends AbstractEvent {

    private Long id;

    public RequestContentDenied(EBook aggregate) {
        super(aggregate);
    }

    public RequestContentDenied() {
        super();
    }
}
//>>> DDD / Domain Event
