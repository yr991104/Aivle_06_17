package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SummarizeContent extends AbstractEvent {

    private Long id;

    public SummarizeContent() {
        super();
    }
}
//>>> DDD / Domain Event
