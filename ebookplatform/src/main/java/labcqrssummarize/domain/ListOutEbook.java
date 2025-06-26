package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ListOutEbook extends AbstractEvent {

    private Long id;

    public ListOutEbook(EBookPlatform aggregate) {
        super(aggregate);
    }

    public ListOutEbook() {
        super();
    }
}
//>>> DDD / Domain Event
