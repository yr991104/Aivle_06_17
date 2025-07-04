package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestContentApproved extends AbstractEvent {

    private String ebookId;
    private String title;
    private String content;
    private PublicationStatus publicationStatus;
    private String authorId;

    public RequestContentApproved(EBook aggregate) {
        super(aggregate);
        
        // 변경될 속성만 this로 명시적으로 세팅
        this.ebookId = aggregate.getEbookId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.authorId = aggregate.getAuthorId();
        this.publicationStatus = aggregate.getPublicationStatus();
    }

    public RequestContentApproved() {
        super();
    }
}
//>>> DDD / Domain Event
