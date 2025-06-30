package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class WrittenContent extends AbstractEvent {

    private Stirng ebookId;
    private String authorId;
    private Boolean isApproved;

    public WrittenContent(Author aggregate) {
        super(aggregate);
        this.id = aggregate.getEbookId();
    }

    public WrittenContent() {
        super();
    }
}
//>>> DDD / Domain Event
