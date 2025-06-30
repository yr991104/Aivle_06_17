package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

// <<< DDD / Domain Event
@Data
@ToString
public class RequestOpenEBookAccept extends AbstractEvent {

    private String subscriberId;
    private String userId;
    private String ebookId;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    /** 이벤트 발행 시 사용 */
    public RequestOpenEBookAccept(Subscriber aggregate, String ebookId) {
        super(aggregate);
        this.subscriberId       = aggregate.getSubscriberId();
        this.userId             = aggregate.getUserId();
        this.subscriptionStatus = aggregate.getSubscriptionStatus();
        this.ebookId            = ebookId;
    }

    
    public RequestOpenEBookAccept() {
        super();
    }
}

//>>> DDD / Domain Event
