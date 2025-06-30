package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ListOutEbookRequested extends AbstractEvent {

    private String eBookId;

    public ListOutEbookRequested(Author aggregate) {
        super(aggregate);
        // 작가의 전자책 ID를 설정
        this.eBookId = aggregate.getEbookId();
    }

    public ListOutEbookRequested() {
        super();
    }
}
//>>> DDD / Domain Event
