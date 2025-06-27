package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.AisystemApplication;
import labcqrssummarize.domain.EstimatiedPriceAndCategory;
import labcqrssummarize.domain.GeneratedEBookCover;
import labcqrssummarize.domain.SummarizedContent;
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

    @PostPersist
    public void onPostPersist() {
        GeneratedEBookCover generatedEBookCover = new GeneratedEBookCover(this);
        generatedEBookCover.publishAfterCommit();

        SummarizedContent summarizedContent = new SummarizedContent(this);
        summarizedContent.publishAfterCommit();

        EstimatiedPriceAndCategory estimatiedPriceAndCategory = new EstimatiedPriceAndCategory(
            this
        );
        estimatiedPriceAndCategory.publishAfterCommit();
    }

    public static EBookRepository repository() {
        EBookRepository eBookRepository = AisystemApplication.applicationContext.getBean(
            EBookRepository.class
        );
        return eBookRepository;
    }
}
//>>> DDD / Aggregate Root
