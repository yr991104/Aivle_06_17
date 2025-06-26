package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GenerateBookCover extends AbstractEvent {

    private Long id;

    public GenerateBookCover() {
        super();
    }
}
//>>> DDD / Domain Event
