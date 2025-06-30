package labcqrssummarize.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/subscribers")
@Transactional
public class SubscriberController {

    @Autowired
    SubscriberRepository subscriberRepository;

    @GetMapping("/status")
    public SubscriberStatusResponse getSubscriberStatus(@RequestParam String userId) {
        SubscriberStatusResponse response = new SubscriberStatusResponse();
        response.setUserId(userId);
        
        try {
            // userId로 구독자 정보 조회
            Optional<Subscriber> subscriberOpt = subscriberRepository.findById(userId);
            
            if (subscriberOpt.isPresent()) {
                Subscriber subscriber = subscriberOpt.get();
                response.setSubscriptionStatus(subscriber.getSubscriptionStatus());
                response.setSuccess(true);
                response.setMessage("구독자 상태 조회 성공");
            } else {
                response.setSuccess(false);
                response.setMessage("구독자를 찾을 수 없습니다: " + userId);
            }
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("구독자 상태 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
}
//>>> Clean Arch / Inbound Adaptor
