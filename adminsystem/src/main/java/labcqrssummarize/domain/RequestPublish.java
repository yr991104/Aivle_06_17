package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestPublish extends AbstractEvent {

    private String ebookId;
    private String authorId;

    public RequestPublish(EBook ebook) {
        super(ebook);
        this.ebookId = ebook.getEbookId();
        this.authorId = ebook.getAuthorId();
    }

    public RequestPublish() {
        super();
    }
}
