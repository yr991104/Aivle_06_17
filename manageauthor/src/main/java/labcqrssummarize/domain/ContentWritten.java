package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ContentWritten extends AbstractEvent {

    private Long id;

    public ContentWritten(Author aggregate) {
        super(aggregate);
    }

    public ContentWritten() {
        super();
    }
}
//>>> DDD / Domain Event
