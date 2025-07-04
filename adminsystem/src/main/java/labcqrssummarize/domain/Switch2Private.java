package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Switch2Private extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private PublicationStatus publicationStatus;

    public Switch2Private(EBook aggregate) {
        super(aggregate);
        this.ebookId = aggregate.getEbookId();
        this.authorId = aggregate.getAuthorId();
        this.publicationStatus = aggregate.getPublicationStatus();
    }

    public Switch2Private() {
        super();
    }
}
//>>> DDD / Domain Event
