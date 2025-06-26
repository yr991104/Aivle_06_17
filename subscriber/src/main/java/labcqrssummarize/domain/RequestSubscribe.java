package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestSubscribe extends AbstractEvent {

    private Long id;

    public RequestSubscribe(Subscriber aggregate) {
        super(aggregate);
    }

    public RequestSubscribe() {
        super();
    }
}
//>>> DDD / Domain Event
