package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@ToString
public class RequestOpenEBookAccept extends AbstractEvent {

    private String subscriberId;
    private String userId;
    
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;
    
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    private String ebookId;

    public RequestOpenEBookAccept() {
        super();
    }
}

