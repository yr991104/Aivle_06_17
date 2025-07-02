package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PublishCanceled extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private PublicationStatus publicationStatus;

    public PublishCanceled(EBook aggregate) {
        super(aggregate);
    }

    public PublishCanceled() {
        super();
    }
}
//>>> DDD / Domain Event
