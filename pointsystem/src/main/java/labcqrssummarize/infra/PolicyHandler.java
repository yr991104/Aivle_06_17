package labcqrssummarize.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import labcqrssummarize.external.EBook;
import labcqrssummarize.external.EBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserPointRepository userPointRepository;

    @Autowired
    EBookService eBookService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SignedUp'"
    )
    public void wheneverSignedUp_CheckMembership(@Payload SignedUp signedUp) {
        SignedUp event = signedUp;
        System.out.println(
            "\n\n##### listener CheckMembership : " + signedUp + "\n\n"
        );

        // Check if user is KT member
        String membershipType = signedUp.getMembershipType() != null ? 
            signedUp.getMembershipType().toString() : "";
        
        Integer pointsToGive;
        String description;
        
        if ("KT".equals(membershipType)) {
            pointsToGive = 5000;
            description = "KT 멤버십 가입 보너스 포인트";
        } else {
            pointsToGive = 1000;
            description = "일반 가입 보너스 포인트";
        }

        GivePointCommand command = new GivePointCommand();
        command.setUserId(signedUp.getUserId());
        command.setPoint(pointsToGive);
        command.setDescription(description);
        
        UserPoint.givePoint(command);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ViewHistory'"
    )
    public void wheneverViewHistory_ReducePointsForReading(@Payload ViewHistory viewHistory) {
        ViewHistory event = viewHistory;
        System.out.println(
            "\n\n##### listener ReducePointsForReading : " + viewHistory + "\n\n"
        );

        String subscriptionType = viewHistory.getSubscriptionType();
        String membershipType = viewHistory.getMembershipType();
        String ebookId = viewHistory.getEbookId();
        
        // Check if user is a member (either normal or KT)
        boolean isMember = "KT".equals(membershipType) || "NORMAL".equals(membershipType);
        
        // Check if user is subscribed
        boolean isSubscribed = "subscribed".equals(subscriptionType);
        
        Integer pointsToReduce;
        String description;
        
        if (!isMember) {
            // Non-members cannot read books
            System.out.println("Non-member user cannot read books: " + viewHistory.getUserId());
            return;
        }
        
        if (isSubscribed) {
            pointsToReduce = 0;
            description = "구독자 무료 읽기";
        } else {
            // Get the actual book price from aisystem
            try {
                EBook ebook = eBookService.getEBook(ebookId);
                Integer bookPrice = ebook != null ? ebook.getPrice() : 0;
                pointsToReduce = bookPrice != null ? bookPrice : 0;
                description = "비구독자 이북 읽기 포인트 차감 (책 가격: " + pointsToReduce + "포인트)";
                System.out.println("Book price retrieved from aisystem: " + pointsToReduce + " for ebook: " + ebookId);
            } catch (Exception e) {
                System.out.println("Failed to get book price from aisystem for ebook: " + ebookId + ", Error: " + e.getMessage());
                pointsToReduce = 0;
                description = "책 가격 정보 조회 실패";
            }
        }

        if (pointsToReduce > 0) {
            ReducePointCommand command = new ReducePointCommand();
            command.setUserId(viewHistory.getUserId());
            command.setPoint(pointsToReduce);
            command.setDescription(description);
            
            UserPoint.reducePoint(command);
        } else {
            // Log free reading for subscribers or when price is 0
            System.out.println("Free reading or zero price: " + viewHistory.getUserId() + ", Points: " + pointsToReduce);
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
