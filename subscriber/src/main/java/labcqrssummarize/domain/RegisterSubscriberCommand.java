package labcqrssummarize.domain;

import lombok.Data;

@Data
public class RegisterSubscriberCommand {
    private String subscriberId;
    private String userId;
    private String password;
    private String email;
    private SubscriptionType subscriptionType;
    private MembershipType membershipType;
}