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
/**
 * PolicyHandler
 * - Kafka로부터 발행되는 Event를 구독하고, 이에 따라 Aggregate의 상태 변화를 유도하는 역할
 * - Pub/Sub 기반 MSA 구조에서 핵심적인 중재자 역할을 수행
 * - 실제 상태 변화는 Aggregate 내부 메서드를 통해서만 가능
 * - 관리자 Command를 대체하거나 보조하는 형태로 동작
 */
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    EBookRepository eBookRepository;

    /**
     * 전체 메시지 로깅 (필요 시 제거 가능)
     */
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    /**
     * [작가 등록 접수] 이벤트 수신
     * - 신규 작가라면 DB에 등록
     * - 기존 작가인 경우 무시 또는 별도 로직 가능
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RegisteredAuthor'"
    )
    public void wheneverRegisteredAuthor_HandleAuthorRegistrationRequest(
        @Payload RegisteredAuthor registeredAuthor
    ) {
        System.out.println("\n\n##### listener HandleAuthorRegistrationRequest : " + registeredAuthor + "\n\n");

        Author author = authorRepository.findById(registeredAuthor.getAuthorId())
            .orElseGet(() -> {
                Author newAuthor = new Author();
                newAuthor.setAuthorId(registeredAuthor.getAuthorId());
                newAuthor.setName(registeredAuthor.getName());
                newAuthor.setUserId(registeredAuthor.getUserId());
                return newAuthor;
            });

        authorRepository.save(author);
    }

    /**
     * [콘텐츠 작성됨] 이벤트 수신
     * - 해당 전자책을 찾아 콘텐츠 승인 처리
     * - 실제 로직에서는 검열 등의 과정 추가 가능
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='WrittenContent'"
    )
    public void wheneverWrittenContent_HandleContentRegistrationRequest(
        @Payload WrittenContent writtenContent
    ) {
        System.out.println("\n\n##### listener HandleContentRegistrationRequest : " + writtenContent + "\n\n");

        EBook ebook = eBookRepository.findById(writtenContent.getEbookId()).orElseThrow();
        ebook.approveContent();
        eBookRepository.save(ebook);
    }

    /**
     * [출간 요청] 이벤트 수신
     * - 출간 요청 도달을 확인
     * - 이후 관리자가 승인/거부를 직접 Command로 수행
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublish'"
    )
    public void wheneverRequestPublish_HandlePublishRequest(
        @Payload RequestPublish requestPublish
    ) {
        System.out.println("\n\n##### listener HandlePublishRequest : " + requestPublish + "\n\n");

        // 출간 요청 수신 후 후속 처리 가능
    }

    /**
     * [전자책 비공개 요청] 이벤트 수신
     * - 해당 전자책을 비공개 상태로 전환
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ListOutEbookRequested'"
    )
    public void wheneverListOutEbookRequested_HandleSwitch2Private(
        @Payload ListOutEbookRequested event
    ) {
        System.out.println("##### listener HandleSwitch2Private : " + event);
    }

    /**
     * [출간 요청 취소] 이벤트 수신
     * - 해당 전자책의 출간을 취소 처리
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublishCanceled'"
    )
    public void wheneverRequestPublishCanceled_HandleRequestPublishCanceled(
        @Payload RequestPublishCanceled event
    ) {
        System.out.println("##### listener HandleRequestPublishCanceled : " + event);
    }
}
