package labcqrssummarize.domain;

import lombok.Data;

//프론트 메인 홈페이지에서 책을 클릭 햇을 때
@Data
public class RequestOpenEBookCommand {
    private String subscriberId;          
    private String userId;                
    private String ebookId;               // 클릭한 전자책 ID
    private SubscriptionStatus subscriptionStatus;  
}