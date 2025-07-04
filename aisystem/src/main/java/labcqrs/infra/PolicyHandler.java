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
    EBookRepository eBookRepository;

    @Autowired
    OpenAIService openAIService;

    @Autowired
    PdfService pdfService;

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublishApproved'"
    )
    @Transactional
    public void wheneverRequestPublishApproved_Handle(@Payload RequestPublishApproved event) {

    if (event.getEbookId() == null) return;

    // DB에 없는 경우만 insert
    if (!eBookRepository.findById(event.getEbookId()).isPresent()) {
        EBook ebook = new EBook();
        ebook.setEbookId(event.getEbookId());
        ebook.setTitle(event.getTitle());
        ebook.setContent(event.getContent());
        ebook.setAuthorId(event.getAuthorId());
        ebook.setPublicationStatus(event.getPublicationStatus());
        eBookRepository.save(ebook);

        System.out.println("✅ 신규 EBook DB 등록 완료: " + event.getEbookId());
    }

    // AI 처리
    try {
        System.out.println("✅ AI 처리 시작: ebookId=" + event.getEbookId());

        EBook ebook = eBookRepository.findById(event.getEbookId()).orElse(null);
        if (ebook == null) {
            System.out.println("❌ EBook not found: " + event.getEbookId());
            return;
        }

        String summary = openAIService.summarizeText(ebook.getContent());
        ebook.generateSummary(summary);

        String coverImageUrl = openAIService.generateCoverImage(ebook.getTitle());
        ebook.generateCoverImage(coverImageUrl);

        String category = openAIService.estimateCategory(summary);
        Integer price = openAIService.estimatePrice(summary);
        ebook.estimatePriceAndCategory(category, price);

        byte[] pdfBytes = pdfService.createPdfFromText(ebook.getTitle(), summary);
        String pdfPath = "pdfs/" + ebook.getEbookId() + ".pdf";
        pdfService.savePdfFile(pdfBytes, pdfPath);
        ebook.setPdfPath(pdfPath);

        eBookRepository.save(ebook);

        System.out.println("✅ AI 처리 및 상태 반영 완료: " + event.getEbookId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}