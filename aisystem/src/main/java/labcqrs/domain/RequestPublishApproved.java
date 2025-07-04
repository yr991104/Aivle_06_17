package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestPublishApproved extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private String title;
    private String content;
    private PublicationStatus publicationStatus;

    public RequestPublishApproved(EBook aggregate) {
        super(aggregate);
        this.ebookId = aggregate.getEbookId();
        this.authorId = aggregate.getAuthorId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.publicationStatus = aggregate.getPublicationStatus();
    }

    public RequestPublishApproved() {
        super();
    }
}