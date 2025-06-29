package labcqrssummarize.infra;
import labcqrssummarize.domain.SubscriberRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.Subscriber;
import labcqrssummarize.domain.SignedUp;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    SubscriberRepository subscriberRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
        // catch-all listener
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SignedUp'"
    )
    public void wheneverSignedUp_RecommandKtMembership(
        @Payload SignedUp signedUp
    ) {
        System.out.println(
            "\n\n##### listener RecommandKtMembership : " + signedUp + "\n\n"
        );
        Subscriber.recommandKtMembership(signedUp);
    }
}
//>>> Clean Arch / Inbound Adaptor
