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
import java.util.UUID;
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

        boolean exists = authorRepository.existsByNameAndUserId(registeredAuthor.getName(), registeredAuthor.getUserId());
    
        if (exists) {
            System.out.println("이미 존재하는 작가입니다. 등록 무시.");
            return;
        }
        Author author = new Author();
        author.setName(registeredAuthor.getName());
        author.setUserId(registeredAuthor.getUserId());
        author.setIsApproved(false);

        // 정책상 등록 신청만으로 자동 승인하는 경우:
        author.approve();
        
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

        EBook ebook = new EBook();
        ebook.setEbookId(UUID.randomUUID().toString());
        ebook.setAuthorId(writtenContent.getAuthorId());
        ebook.setTitle(writtenContent.getTitle());
        ebook.setContent(writtenContent.getContent());
        ebook.setPublicationStatus(PublicationStatus.CONTINUED); // 일단 대기 상태로 생성

        eBookRepository.save(ebook);

        System.out.println("📚 신규 EBook 생성 완료 : " + ebook.getEbookId());
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
    /**
     * [출간 요청] 이벤트 수신
     * - 출간 요청 도달을 확인
     * - 이후 관리자가 승인/거부를 직접 Command로 수행
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublish'"
    )
    public void wheneverRequestPublish_HandlePublishRequest(@Payload RequestPublish event) {
        System.out.println("\n\n##### listener HandlePublishRequest : " + event + "\n\n");

        eBookRepository.findById(event.getEbookId()).ifPresent(ebook -> {
            // 출간 심사 로직 → 여기선 단순히 바로 승인 예시
            ebook.approvePublish();  // 내부에서 상태 변경 + 이벤트 발행
            eBookRepository.save(ebook);
        });
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='GeneratedEBookCover'"
    )
    public void wheneverGeneratedEBookCover(@Payload GeneratedEBookCover event) {
        if (!event.validate()) return;

        eBookRepository.findById(event.getEbookId()).ifPresent(ebook -> {
        ebook.setCoverImage(event.getCoverImage());
        eBookRepository.save(ebook);
        });
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SummarizedContent'"
    )
    public void wheneverSummarizedContent(@Payload SummarizedContent event) {
        if (!event.validate()) return;

        eBookRepository.findById(event.getEbookId()).ifPresent(ebook -> {
        ebook.setSummary(event.getSummary());
        eBookRepository.save(ebook);
        });
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='EstimatedPriceAndCategory'"
    )
    public void wheneverEstimatedPriceAndCategory(@Payload EstimatedPriceAndCategory event) {
        if (!event.validate()) return;

        eBookRepository.findById(event.getEbookId()).ifPresent(ebook -> {
        ebook.setPrice(event.getPrice());
        ebook.setCategory(event.getCategory());
        eBookRepository.save(ebook);
        });
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
