package labcqrssummarize.domain;

import java.time.LocalDate;
import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SignUp extends AbstractEvent {

    private Long id;

    public SignUp(Subscriber aggregate) {
        super(aggregate);
    }

    public SignUp() {
        super();
    }
}
//>>> DDD / Domain Event
