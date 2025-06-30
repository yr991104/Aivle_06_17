package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RequestPublishApproved extends AbstractEvent {

    private String ebookId;
    private String authorId;
    private String title;      // 추가
    private String content;    // 추가
    private Object publicationStatus;
}