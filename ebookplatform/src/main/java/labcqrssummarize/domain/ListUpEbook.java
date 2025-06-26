package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ListUpEbook extends AbstractEvent {

    private Long id;

    public ListUpEbook(EBookPlatform aggregate) {
        super(aggregate);
    }

    public ListUpEbook() {
        super();
    }
}
//>>> DDD / Domain Event
