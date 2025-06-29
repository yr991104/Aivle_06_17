package labcqrssummarize.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.SignedUp;
import labcqrssummarize.domain.SubscriberRepository;  

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
        System.out.println("##### listener SignedUp : " + signedUp + "\n");
        // 이후 필요하면 정책 로직 추가
    }
}

