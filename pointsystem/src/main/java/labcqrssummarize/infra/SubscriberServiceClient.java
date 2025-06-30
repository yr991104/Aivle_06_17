package labcqrssummarize.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "subscriber", url = "${api.url.subscriber}")
public interface SubscriberServiceClient {
    
    @RequestMapping(method = RequestMethod.GET, path = "/subscribers/{userId}")
    SubscriberInfo getSubscriberInfo(@PathVariable("userId") String userId);
    
    public static class SubscriberInfo {
        private String userId;
        private String subscriptionStatus;
        private String membershipType;
        
        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getSubscriptionStatus() { return subscriptionStatus; }
        public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }
        
        public String getMembershipType() { return membershipType; }
        public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    }
} 