package labcqrssummarize.infra;

import labcqrssummarize.domain.SubscriberStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubscriberService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String SUBSCRIBER_SERVICE_URL = "http://subscriber:8080/subscribers/status";

    public SubscriberStatusResponse getSubscriberStatus(String userId) {
        try {
            // GET 요청으로 구독자 상태 확인 (URL 파라미터 사용)
            return restTemplate.getForObject(
                SUBSCRIBER_SERVICE_URL + "?userId=" + userId, 
                SubscriberStatusResponse.class
            );
        } catch (Exception e) {
            System.out.println("Subscriber 상태 조회 실패: " + e.getMessage());
            
            // 오류 시 기본 응답 생성
            SubscriberStatusResponse errorResponse = new SubscriberStatusResponse();
            errorResponse.setUserId(userId);
            errorResponse.setSuccess(false);
            errorResponse.setMessage("구독자 상태 조회 중 오류가 발생했습니다: " + e.getMessage());
            return errorResponse;
        }
    }
} 