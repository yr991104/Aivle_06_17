package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PostPersist;
import labcqrssummarize.AisystemApplication;
import labcqrssummarize.external.GptClient;
import labcqrssummarize.infra.ApplicationContextProvider;
import lombok.Data;

@Entity
@Table(name = "EBook_table")
@Data
//<<< DDD / Aggregate Root
public class EBook {

    @Id
    private String ebookId;

    private String title;
    private String authorId;
    private String content;
    private String coverImage;
    private String summary;
    private Boolean isPublicationApproved;
    private Integer price;
    private String category;
    private Integer countViews;
    private publicationStatus publicationStatus;

    // ✅ AI 표지 생성
    public static void generateEBookCover(GenerateEBookCoverCommand command) {
        GptClient gptClient = ApplicationContextProvider.getBean(GptClient.class);

        String prompt = "책 제목: " + command.getTitle() + "\n책 내용 요약: " + command.getContent()
                + "\n위 내용을 기반으로 어울리는 책 표지 이미지를 설명하고, 이미지 URL을 반환해줘.";

        String imageUrl = gptClient.callGpt(prompt);

        GeneratedEBookCover event = new GeneratedEBookCover();
        event.setEbookId(command.getEbookId());
        event.setTitle(command.getTitle());
        event.setAuthorId(command.getAuthorId());
        event.setContent(command.getContent());
        event.setCoverImage(imageUrl);

        event.publishAfterCommit();
    }

    // ✅ 자동 요약
    public static void summarizeContent(SummarizeContentCommand command) {
        GptClient gptClient = ApplicationContextProvider.getBean(GptClient.class);

        String prompt = "다음 책 내용을 3~5줄로 요약해줘:\n\n" + command.getContent();
        String summary = gptClient.callGpt(prompt);

        SummarizedContent event = new SummarizedContent();
        event.setEbookId(command.getEbookId());
        event.setTitle(command.getTitle());
        event.setContent(command.getContent());
        event.setSummary(summary);

        event.publishAfterCommit();
    }

    // ✅ 자동 분류 및 가격 책정
    public static void estimatePriceAndCategory(EstimatePriceAndCategoryCommand command) {
        GptClient gptClient = ApplicationContextProvider.getBean(GptClient.class);

        String prompt = "책 요약: " + command.getSummary()
                + "\n이 책에 어울리는 장르 카테고리(예: 로맨스, 스릴러, 자기계발 등)를 알려주고, 월 구독 가격(원)을 숫자 하나로 제시해줘. 예시: '카테고리: 자기계발 / 가격: 3900원'";
        String result = gptClient.callGpt(prompt);

        EstimatiedPriceAndCategory event = new EstimatiedPriceAndCategory();
        event.setEbookId(command.getEbookId());
        event.setSummary(command.getSummary());
        event.setContent(command.getContent());

        if (result.contains("카테고리") && result.contains("가격")) {
            try {
                String[] parts = result.split("/");
                event.setCategory(parts[0].split(":")[1].trim());
                event.setPrice(Integer.parseInt(parts[1].split(":")[1].replace("원", "").trim()));
            } catch (Exception e) {
                event.setCategory("기타");
                event.setPrice(2900);
            }
        }

        event.publishAfterCommit();
    }

    public static EBookRepository repository() {
        return AisystemApplication.applicationContext.getBean(EBookRepository.class);
    }
}
//>>> DDD / Aggregate Root
