package labcqrssummarize.domain;

import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class SignedUp extends AbstractEvent {

    private String userId;
    private String subscriptionType;
    private Object viewHistory;
    private membershipType membershipType;
    private Object subscriptionStatus;
    private String password;
    private Object email;
    private String subscriberId;
}
