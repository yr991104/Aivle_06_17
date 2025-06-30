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
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value = "/userPoints")
@Transactional
public class UserPointController {

    @Autowired
    UserPointRepository userPointRepository;
    
    @Autowired
    SubscriberServiceClient subscriberServiceClient;

    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<UserPoint>> getUserPoint(@PathVariable String userId) {
        UserPoint userPoint = userPointRepository.findById(userId).orElse(null);
        
        if (userPoint == null) {
            return ResponseEntity.notFound().build();
        }
        
        EntityModel<UserPoint> resource = EntityModel.of(userPoint);
        resource.add(Link.of("/userPoints/" + userId).withSelfRel());
        
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/viewEbook")
    public ResponseEntity<String> viewEbook(@RequestBody EbookViewRequest request) {
        try {
            // Subscriber 서비스에서 구독 상태 조회
            SubscriberServiceClient.SubscriberInfo subscriberInfo = 
                subscriberServiceClient.getSubscriberInfo(request.getUserId());
            
            if (subscriberInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + request.getUserId());
            }
            
            // 구독 상태에 따른 포인트 차감
            Integer deductPoints = 0;
            String description = "";
            
            if ("SUBSCRIBED".equals(subscriberInfo.getSubscriptionStatus())) {
                // 구독자: 포인트 차감 없음
                deductPoints = 0;
                description = "EBook viewed (Subscribed user - no points deducted)";
            } else {
                // 일반 사용자: 가격만큼 포인트 차감
                deductPoints = request.getPrice();
                description = "EBook viewed - " + request.getEbookId() + " (Price: " + request.getPrice() + " points)";
            }
            
            if (deductPoints > 0) {
                // 포인트 차감 실행
                ReducePointCommand command = new ReducePointCommand();
                command.setUserId(request.getUserId());
                command.setPoint(deductPoints);
                command.setDescription(description);
                
                UserPoint.reducePoint(command);
            }
            
            return ResponseEntity.ok("EBook viewed successfully. Points deducted: " + deductPoints);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing ebook view: " + e.getMessage());
        }
    }
    
    public static class EbookViewRequest {
        private String userId;
        private String ebookId;
        private Integer price;
        
        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getEbookId() { return ebookId; }
        public void setEbookId(String ebookId) { this.ebookId = ebookId; }
        
        public Integer getPrice() { return price; }
        public void setPrice(Integer price) { this.price = price; }
    }
}
//>>> Clean Arch / Inbound Adaptor
