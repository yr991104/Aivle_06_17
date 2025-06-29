package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscribeCanceled extends AbstractEvent {

    private String subscriberId;
    private String userId;
    private String password;
    private String email;
    private SubscriptionType subscriptionType;    // enum 타입으로 변경
    private MembershipType membershipType;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;
    private List<ViewHistory> viewHistory;

    public SubscribeCanceled(Subscriber aggregate) {
        super(aggregate);
        this.subscriberId       = aggregate.getSubscriberId();
        this.userId             = aggregate.getUserId();
        this.password           = aggregate.getPassword();
        this.email              = aggregate.getEmail();
        this.subscriptionType   = aggregate.getSubscriptionType();  // enum 할당
        this.membershipType     = aggregate.getMembershipType();
        this.subscriptionStatus = aggregate.getSubscriptionStatus();
        this.startedAt          = aggregate.getStartedAt();
        this.expiredAt          = aggregate.getExpiredAt();
        this.viewHistory        = aggregate.getViewHistory();
    }

    public SubscribeCanceled() {
        super();
    }
}
//>>> DDD / Domain Event


