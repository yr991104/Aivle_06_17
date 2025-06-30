package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GeneratedEBookCover extends AbstractEvent {

    private String ebookId;
    private String title;
    private String authorId;
    private String coverImage;
    private String content;

    public GeneratedEBookCover(EBook aggregate) {
        super(aggregate);
    }

    public GeneratedEBookCover() {
        super();
    }
}
//>>> DDD / Domain Event