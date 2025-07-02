package labcqrssummarize.domain;

import lombok.Data;
import lombok.ToString;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;

@Data
@ToString
public class RequestPublishCanceled extends AbstractEvent {

    private Long id;
}
