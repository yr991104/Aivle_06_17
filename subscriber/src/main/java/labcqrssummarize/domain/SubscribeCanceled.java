package labcqrssummarize.domain;

import java.time.LocalDateTime;
import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscribeCanceled extends AbstractEvent {

    private String subscriberId;
    private String userId;
    private String subscriptionType;
    private LocalDateTime expiredAt;
    private SubscriptionStatus subscriptionStatus;
    private String email;

    public SubscribeCanceled(Subscriber aggregate) {
        super(aggregate);
        this.subscriberId = aggregate.getSubscriberId();
        this.userId = aggregate.getUserId();
        this.subscriptionType = aggregate.getSubscriptionType();
        this.expiredAt = aggregate.getExpiredAt();
        this.subscriptionStatus = aggregate.getSubscriptionStatus();
        this.email = aggregate.getEmail();
    }

    public SubscribeCanceled() {
        super();
    }
}
//>>> DDD / Domain Event

