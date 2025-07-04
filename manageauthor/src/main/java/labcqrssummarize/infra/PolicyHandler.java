package labcqrssummarize.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.transaction.Transactional;

import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    AuthorRepository authorRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 1. 작가 등록됨 → 승인 상태 초기화
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestAuthorApproved'"
    )
    public void wheneverRequestAuthorApproved(@Payload RequestAuthorApproved event) {
        if (!event.validate()) return;

        System.out.println("✅ 작가 승인 완료됨: " + event.toJson());

        // 1. userId 기준으로 Author 찾기
        Author author = authorRepository.findByUserId(event.getUserId())
            .orElseThrow(() -> new RuntimeException("해당 유저의 작가를 찾을 수 없습니다."));

        // 2. adminsystem에서 넘어온 authorId와 isApproved 상태 반영
        author.setAuthorId(event.getAuthorId());
        author.setIsApproved(event.getIsApproved());

        authorRepository.save(author);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestContentApproved'"
    )
    public void wheneverRequestContentApproved_UpdateEbooks(@Payload RequestContentApproved event) {
        if (!event.validate()) return;

        System.out.println("📚 eBook 등록 이벤트 수신 : " + event.toJson());

        authorRepository.findByAuthorId(event.getAuthorId()).ifPresent(author -> {
            author.getEbooks().add(event.getEbookId());
            authorRepository.save(author);
        });
    }


    // 3. 출간 요청됨
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestPublish(@Payload RequestPublish event) {
        if (!event.validate()) return;
        System.out.println("📤 출간 요청됨: " + event.toJson());

        // 관리자 시스템으로 이벤트 전달 (이 컨텍스트에서는 저장 없음)
    }

    // 4. 출간 요청 취소됨
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestPublishCanceled(@Payload RequestPublishCanceled event) {
        if (!event.validate()) return;
        System.out.println("❌ 출간 요청 취소됨: " + event.toJson());

        // 필요시 관리자 시스템에 이벤트 전달
    }

    // 5. 전자책 비공개
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverListOutEbookRequested(@Payload ListOutEbookRequested event) {
        if (!event.validate()) return;
        System.out.println("🚫 전자책 비공개 요청 수신됨: " + event.toJson());

        // 이 컨텍스트에서는 처리 로직 없음
    }
}
//>>> Clean Arch / Inbound Adaptor
