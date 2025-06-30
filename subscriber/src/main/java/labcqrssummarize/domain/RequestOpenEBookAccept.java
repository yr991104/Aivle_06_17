package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

// <<< DDD / Domain Event
@Data
@ToString
public class RequestOpenEBookAccept extends AbstractEvent {

    private String subscriberId;
    private String userId;
    private ViewHistory viewHistory;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    public RequestOpenEBookAccept(Subscriber aggregate) {
        super(aggregate);
    }

    public RequestOpenEBookAccept() {
        super();
    }
}

//>>> DDD / Domain Event
