package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.persistence.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.AisystemApplication;
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

    @Column(length = 1000)
    private String coverImage;

    private String summary;

    private Integer price;

    private String category;

    private Integer countViews;

    private String pdfPath; // PDF 경로 필드 추가

    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus;

    @PostPersist
    public void onPostPersist() {
        RequestPublishApproved requestPublishApproved = new RequestPublishApproved(this);
        requestPublishApproved.publishAfterCommit();

        GeneratedEBookCover generatedEBookCover = new GeneratedEBookCover(this);
        generatedEBookCover.publishAfterCommit();

        SummarizedContent summarizedContent = new SummarizedContent(this);
        summarizedContent.publishAfterCommit();

        EstimatedPriceAndCategory estimatedPriceAndCategory = new EstimatedPriceAndCategory(this);
        estimatedPriceAndCategory.publishAfterCommit();
    }

    public static EBookRepository repository() {
        return AisystemApplication.applicationContext.getBean(EBookRepository.class);
    }
    public void generateSummary(String summary) {
        this.summary = summary;

        SummarizedContent event = new SummarizedContent(this);
        event.publishAfterCommit();
    }


    public void generateCoverImage(String coverImageUrl) {
        this.coverImage = coverImageUrl;

        GeneratedEBookCover event = new GeneratedEBookCover(this);
        event.publishAfterCommit();
    }


    public void estimatePriceAndCategory(String category, Integer price) {
        this.category = category;
        this.price = price;

        EstimatedPriceAndCategory event = new EstimatedPriceAndCategory(this);
        event.publishAfterCommit();
    }


    // PDF 생성 및 경로 저장 로직
    public void generatePdf(byte[] pdfBytes) {
        try {
            Path pdfDir = Path.of("pdfs");
            if (!Files.exists(pdfDir)) Files.createDirectories(pdfDir);
            Path pdfPath = pdfDir.resolve(this.ebookId + ".pdf");
            try (FileOutputStream fos = new FileOutputStream(pdfPath.toFile())) {
                fos.write(pdfBytes);
            }
            this.pdfPath = pdfPath.toAbsolutePath().toString();
            System.out.println("✅ PDF 저장 완료: " + this.pdfPath);
        } catch (IOException e) {
            throw new RuntimeException("PDF 저장 실패", e);
        }
    }
}