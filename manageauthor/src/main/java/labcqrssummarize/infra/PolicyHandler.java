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

    @Autowired
    KafkaProcessor kafkaProcessor;

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

        // 전자책 등록 Command 생성
        EbookRegisterCommand command = new EbookRegisterCommand();
        command.setAuthorId(event.getAuthorId());
        command.setTitle(event.getTitle());
        command.setContent(event.getContent());
        // 요약, 카테고리, 가격은 기본값 또는 AI/후처리에서 설정
        command.setSummary("요약 없음"); // 임시값, AI 컨텍스트에서 후처리
        command.setCategory("기타");
        command.setPrice(0);

        // 메시지 발행 (전자책 시스템으로)
        kafkaProcessor.output().send(
                MessageBuilder.withPayload(command).build()
        );
    }

    // 3. 출간 요청됨 → RequestPublish 이벤트 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestPublish(@Payload RequestPublish event) {
        if (!event.validate()) return;

        System.out.println("📤 출간 요청됨: " + event.toJson());

        RequestPublishCommand command = new RequestPublishCommand();
        command.setAuthorId(event.getAuthorId());
        command.setEbookId(event.getEbookId());
        command.setTitle(event.getTitle());

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

    // 5. 전자책 비공개 요청 수신
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverListOutEbookRequested(@Payload ListOutEbookRequested event) {
        if (!event.validate()) return;

        System.out.println("🚫 전자책 비공개 요청 수신됨: " + event.toJson());

        // 작가관리 시스템에서 따로 처리할 로직 없음
    }
}
//>>> Clean Arch / Inbound Adaptor
