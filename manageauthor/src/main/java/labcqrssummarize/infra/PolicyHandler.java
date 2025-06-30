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
    AuthorRepository authorRepository;

    // 기본 수신 핸들러(무시 가능)
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 1. 작가 등록됨 → 작가 상태 변경 (심사 대기)
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRegisteredAuthor(@Payload RegisteredAuthor event) {
        if (!event.validate()) return;

        System.out.println("📩 작가 등록됨: " + event.toJson());

        Author author = authorRepository.findById(event.getAuthorId())
                .orElseThrow(() -> new RuntimeException("해당 작가를 찾을 수 없습니다: " + event.getAuthorId()));
        author.setIsApproved(false); // 심사 대기 상태
        authorRepository.save(author);
    }

    // 2. 콘텐츠 작성됨 → WrittenContent 이벤트 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverWrittenContent(@Payload WrittenContent event) {
        if (!event.validate()) return;

        System.out.println("📝 콘텐츠 작성됨: " + event.toJson());

        // 커맨드 객체 발행
        WrittenContent command = new WrittenContent();
        command.setAuthorId(event.getAuthorId());
        command.setContent(event.getContent());

        command.publish();
    }

    // 3. 출간 요청됨 → RequestPublish 이벤트 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestPublish(@Payload RequestPublish event) {
        if (!event.validate()) return;

        System.out.println("📤 출간 요청됨: " + event.toJson());

        RequestPublish command = new RequestPublish();
        command.setAuthorId(event.getAuthorId());
        command.setEbookId(event.getEbookId());

        command.publish();
    }

    // 4. 출간 요청 취소됨 → RequestPublishCanceled 이벤트 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestPublishCanceled(@Payload RequestPublishCanceled event) {
        if (!event.validate()) return;

        System.out.println("❌ 출간 요청 취소됨: " + event.toJson());

        RequestPublishCanceled command = new RequestPublishCanceled();
        command.setAuthorId(event.getAuthorId());
        command.setEbookId(event.getEbookId());

        command.publish();
    }

    // 5. 전자책 비공개 요청됨 → 작가 시스템은 이 이벤트를 그냥 수신만 하면 됨
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverListOutEbookRequested(@Payload ListOutEbookRequested event) {
        if (!event.validate()) return;

        System.out.println("🚫 전자책 비공개 요청 수신됨: " + event.toJson());

        // 작가관리 시스템에선 별도로 처리할 로직 없음
        // 전자책 비공개 처리는 서재 플랫폼 Bounded Context에서 수행
    }
}
//>>> Clean Arch / Inbound Adaptor
