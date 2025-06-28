package labcqrssummarize.domain;

import lombok.Data;

@Data
public class Subscriber {
    private String subscriberId;
    private String userId;
    private String subscriptionType;
    private String membershipType;
    private String subscriptionStatus;
} 