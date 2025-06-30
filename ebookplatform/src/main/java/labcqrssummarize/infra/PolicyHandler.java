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
    EBookPlatformRepository eBookPlatformRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='GeneratedEBookCover'"
    )
    public void wheneverGeneratedEBookCover_CheckEBookStatus(
        @Payload GeneratedEBookCover generatedEBookCover
    ) {
        
        System.out.println("##### [GeneratedEBookCover] received: " + generatedEBookCover);
        // Comments //
        //AI 표지 생성과 전자책 요약이 모두 된 상태인지 체크

        // Sample Logic //

        EBookPlatform ebook = EBookPlatform.repository().findById(
            Integer.parseInt(generatedEBookCover.getEbookId())
        ).orElse(null);
        if (ebook == null) return;

        ebook.markCoverGenerated();
        if (ebook.isReadyForPublish()) {
            ebook.register();
        }

        eBookPlatformRepository.save(ebook);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SummarizedContent'"
    )
    public void wheneverSummarizedContent_CheckEBookStatus(
        @Payload SummarizedContent summarizedContent
    ) {
        System.out.println("##### [SummarizedContent] received: " + summarizedContent);

        EBookPlatform ebook = EBookPlatform.repository().findById(
            Integer.parseInt(summarizedContent.getEbookId())
        ).orElse(null);
        if (ebook == null) return;

        ebook.markContentSummarized();
        if (ebook.isReadyForPublish()) {
            ebook.register();
        }

        eBookPlatformRepository.save(ebook);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='EstimatiedPriceAndCategory'"
    )
    public void wheneverEstimatiedPriceAndCategory_CheckEBookStatus(
        @Payload EstimatiedPriceAndCategory estimatiedPriceAndCategory
    ) {
        System.out.println("##### [EstimatiedPriceAndCategory] received: " + estimatiedPriceAndCategory);

        EBookPlatform ebook = EBookPlatform.repository().findById(
            Integer.parseInt(estimatiedPriceAndCategory.getEbookId())
        ).orElse(null);
        if (ebook == null) return;

        ebook.markPriceAndCategorySet();
        if (ebook.isReadyForPublish()) {
            ebook.register();
        }

        eBookPlatformRepository.save(ebook);

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverEBookPlatformOpened_LogSuccess(
        @Payload EBookPlatformOpened opened
    ) {
        System.out.println("\n\n📘 전자책 열람 성공 처리됨: " + opened + "\n\n");
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverEBookPlatformRegistered_LogFail(
        @Payload EBookPlatformRegistered registered
    ) {
        System.out.println("\n\n⚠️ 전자책 열람 실패 처리됨(등록만 되어 있고 OPEN 아님): " + registered + "\n\n");
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ListOutEbookRequested'"
    )
    public void wheneverListOutEbookRequested_RequestPrivateStatus(
        @Payload ListOutEbookRequested listOutEbookRequested
    ) {
        System.out.println("##### [ListOutEbookRequested] received: " + listOutEbookRequested);

        ListOutEBookCommand command = new ListOutEBookCommand();
        EBookPlatform.listOutEBook(command);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestOpenEBookAccept'"
    )
    public void wheneverRequestOpenEBookAccept_RequestOpenEBook(
        @Payload RequestOpenEBookAccept requestOpenEBookAccept
    ) {
        System.out.println("##### [RequestOpenEBookAccept] received: " + requestOpenEBookAccept);

        OpenEBookCommand command = new OpenEBookCommand();
        EBookPlatform.openEBook(command);
    }
}
