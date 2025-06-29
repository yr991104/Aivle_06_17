package labcqrssummarize.domain;

import lombok.Data;

@Data
public class RegisterSubscriptionCommand {
    private SubscriptionType subscriptionType;
}