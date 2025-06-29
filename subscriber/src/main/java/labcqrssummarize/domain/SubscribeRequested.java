package labcqrssummarize.domain;

import java.time.LocalDateTime;
import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

//<<< DDD / Domain Event: 구독 요청 발생
@Data
@ToString
public class SubscribeRequested extends AbstractEvent {

    private String subscriberId;
    private String userId;
    private String email;
    private SubscriptionType subscriptionType;
    private MembershipType membershipType;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;

    public SubscribeRequested(Subscriber aggregate) {
        super(aggregate);
        this.subscriberId      = aggregate.getSubscriberId();
        this.userId            = aggregate.getUserId();
        this.email             = aggregate.getEmail();
        this.subscriptionType  = aggregate.getSubscriptionType();
        this.membershipType    = aggregate.getMembershipType();
        this.subscriptionStatus= aggregate.getSubscriptionStatus();
        this.startedAt         = aggregate.getStartedAt();
        this.expiredAt         = aggregate.getExpiredAt();
    }

    public SubscribeRequested() {
        super();
    }
}

