package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoggedIn extends AbstractEvent {
    private String subscriberId;
    private String userId;

    public LoggedIn(Subscriber aggregate) {
        super(aggregate);
        this.subscriberId = aggregate.getSubscriberId();
        this.userId       = aggregate.getUserId();
    }

    // 빈 생성자 필요시
    public LoggedIn() { super(); }
}