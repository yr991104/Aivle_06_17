package labcqrssummarize.domain;

import labcqrssummarize.domain.EBook;
import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ListOutEbookRequested extends AbstractEvent {
    private Long id;
}
