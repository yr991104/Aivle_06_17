package labcqrssummarize.infra;

import javax.transaction.Transactional;

import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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
    public void wheneverSignedUp_CheckMembership(@Payload SignedUp event) {
        System.out.println("\n\n##### [포인트 시스템] SignedUp 수신됨 : " + event + "\n\n");

        if (event.getUserId() == null || event.getMembershipType() == null) {
            System.out.println("[포인트 지급 실패] 이벤트 필드 누락: " + event);
            return;
        }

        // 포인트 지급 로직
        GivePointCommand command = new GivePointCommand();
        command.setUserId(event.getUserId());

        if (event.getMembershipType() == MembershipType.KT) {
            command.setPoint(5000);
            System.out.println("[지급] KT 멤버십 가입자 → 5000 포인트");
        } else {
            command.setPoint(1000);
            System.out.println("[지급] 일반 가입자 → 1000 포인트");
        }

        UserPoint.givePoint(command);
    }
}
