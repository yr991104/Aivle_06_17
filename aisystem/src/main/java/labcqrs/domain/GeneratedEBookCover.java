package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import labcqrssummarize.domain.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class GeneratedEBookCover extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private String coverImage;

    public GeneratedEBookCover(EBook aggregate) {
        super(aggregate);
        this.ebookId = aggregate.getEbookId();
        this.authorId = aggregate.getAuthorId();
        this.coverImage = aggregate.getCoverImage();
    }

    public GeneratedEBookCover() {
        super();
    }
}
//>>> DDD / Domain Event