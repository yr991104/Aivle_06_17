package labcqrssummarize.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.SignedUp;
import labcqrssummarize.domain.SubscriberRepository;  
import labcqrssummarize.domain.MembershipType;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
        // Generic listener
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SignedUp'"
    )
    public void wheneverSignedUp(@Payload SignedUp signedUp) {
        System.out.println("##### listener SignedUp invoked: " + signedUp + "\n");
        // 가입 직후 멤버쉽 타입이 kt가 아닌 경우 가입 추천
        if (signedUp.getMembershipType() == null || signedUp.getMembershipType() == MembershipType.NORMAL) {
            System.out.println("KT 멤버십 가입을 추천합니다 for user: " + signedUp.getUserId());
        }
    }
}

