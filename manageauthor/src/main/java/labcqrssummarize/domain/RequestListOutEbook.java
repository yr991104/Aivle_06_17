package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestListOutEbook extends AbstractEvent {

    private Long id;

    public RequestListOutEbook(Author aggregate) {
        super(aggregate);
    }

    public RequestListOutEbook() {
        super();
    }
}
//>>> DDD / Domain Event
