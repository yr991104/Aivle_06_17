package labcqrssummarize.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserPointRepository userPointRepository;

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

        // 멤버십 타입에 따른 포인트 지급
        Integer pointsToGive = 0;
        String description = "";
        
        if ("KT".equals(event.getMembershipType())) {
            pointsToGive = 5000;
            description = "KT membership signup bonus";
        } else if ("NORMAL".equals(event.getMembershipType())) {
            pointsToGive = 1000;
            description = "Normal membership signup bonus";
        }
        
        if (pointsToGive > 0) {
            GivePointCommand command = new GivePointCommand();
            command.setUserId(event.getUserId());
            command.setPoint(pointsToGive);
            command.setDescription(description);
            
            UserPoint.givePoint(command);
        }
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='HandleEBookViewed'"
    )
    public void wheneverHandleEBookViewed_ReducePointRequested(
        @Payload HandleEBookViewed handleEBookViewed
    ) {
        HandleEBookViewed event = handleEBookViewed;
        System.out.println(
            "\n\n##### listener ReducePointRequested : " +
            handleEBookViewed +
            "\n\n"
        );

        // 이 이벤트는 Kafka를 통해 들어오는 것이므로, 
        // 실제 포인트 차감은 REST API를 통해 처리됩니다.
        // 여기서는 로깅만 수행합니다.
        System.out.println("EBook viewed event received for user: " + event.getUserId() + 
                          ", ebook: " + event.getEbookId() + ", price: " + event.getPrice());
    }
}
//>>> Clean Arch / Inbound Adaptor
