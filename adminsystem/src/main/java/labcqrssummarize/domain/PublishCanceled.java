package labcqrs.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrs.domain.*;
import labcqrs.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PublishCanceled extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private publicationStatus publicationStatus;

    public PublishCanceled(EBook aggregate) {
        super(aggregate);
    }

    public PublishCanceled() {
        super();
    }
}
//>>> DDD / Domain Event
