package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Data
@ToString
public class SignedUp extends AbstractEvent {

    private String subscriberId;
    private String userId;
    private String password;
    private String email;
    private String subscriptionType;  
    private MembershipType membershipType;
    private SubscriptionStatus subscriptionStatus;

    public SignedUp(Subscriber aggregate) {
        super(aggregate);
        this.subscriberId = aggregate.getSubscriberId();  
        this.userId = aggregate.getUserId();
        this.password = aggregate.getPassword();
        this.email = aggregate.getEmail();
        this.subscriptionType = null;  
        this.membershipType = MembershipType.NEW_MEMBER;  
        this.subscriptionStatus = SubscriptionStatus.SUBSCRIBED;  
    }

    public SignedUp() {
        super();
    }
}

