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
    private String authorId;

    public RequestContentApproved(String ebookId, String title, String content, String authorId) {
        this.ebookId = ebookId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    public RequestContentApproved() {
        super();
    }
}
