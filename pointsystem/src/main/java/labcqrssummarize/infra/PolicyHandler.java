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

        // Comments //
        //KT 멤버쉽 가입했는지 확인

        // 멤버십 타입에 따라 포인트 지급
        GivePointCommand command = new GivePointCommand();
        command.setUserId(event.getUserId());
        
        // membershipType enum으로 직접 비교
        if (event.getMembershipType() == membershipType.KT) {
            command.setPoint(5000); // KT 멤버십: 5000포인트 지급
            System.out.println("KT 멤버십 가입 - 5000포인트 지급");
        } else {
            command.setPoint(1000); // NORMAL 멤버십: 1000포인트 지급
            System.out.println("NORMAL 멤버십 가입 - 1000포인트 지급");
        }
        
        UserPoint.givePoint(command);
    }

    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='HandleEBookViewed'"
    // )
    // public void wheneverHandleEBookViewed_ReducePointRequested(
    //     @Payload HandleEBookViewed handleEBookViewed
    // ) {
    //     HandleEBookViewed event = handleEBookViewed;
    //     System.out.println(
    //         "\n\n##### listener ReducePointRequested : " +
    //         handleEBookViewed +
    //         "\n\n"
    //     );

    //     // Sample Logic //

    //     ReducePointCommand command = new ReducePointCommand();
    //     UserPoint.reducePoint(command);
    // }
}
//>>> Clean Arch / Inbound Adaptor
