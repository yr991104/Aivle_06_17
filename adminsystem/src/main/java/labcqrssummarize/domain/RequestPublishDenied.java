package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestPublishDenied extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private publicationStatus publicationStatus;

    public RequestPublishDenied(EBook aggregate) {
        super(aggregate);
    }

    public RequestPublishDenied() {
        super();
    }
}
//>>> DDD / Domain Event
