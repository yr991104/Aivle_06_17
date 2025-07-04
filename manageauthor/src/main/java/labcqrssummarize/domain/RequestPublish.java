package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RequestPublish extends AbstractEvent {

    private String authorId;
    private String ebookId;

    public RequestPublish(Author aggregate, String ebookId) {
        super(aggregate);
        this.authorId = aggregate.getAuthorId();
        this.ebookId = ebookId;
    }

    public RequestPublish() {
        super();
    }
}
//>>> DDD / Domain Event
