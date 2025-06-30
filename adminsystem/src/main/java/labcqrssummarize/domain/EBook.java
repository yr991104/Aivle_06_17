package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.AdminsystemApplication;
import labcqrssummarize.domain.RequestContentApporved;
import labcqrssummarize.domain.RequestContentDenied;
import labcqrssummarize.domain.RequestPublishApproved;
import labcqrssummarize.domain.RequestPublishDenied;
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

    private Integer price;

    private String category;

    private Integer countViews;

    private publicationStatus publicationStatus;

    @PreUpdate
    public void onPreUpdate() {
        RequestContentApporved requestContentApporved = new RequestContentApporved(
            this
        );
        requestContentApporved.publishAfterCommit();

        RequestPublishApproved requestPublishApproved = new RequestPublishApproved(
            this
        );
        requestPublishApproved.publishAfterCommit();

        RequestPublishDenied requestPublishDenied = new RequestPublishDenied(
            this
        );
        requestPublishDenied.publishAfterCommit();

        RequestContentDenied requestContentDenied = new RequestContentDenied(
            this
        );
        requestContentDenied.publishAfterCommit();
    }

    public static EBookRepository repository() {
        EBookRepository eBookRepository = AdminsystemApplication.applicationContext.getBean(
            EBookRepository.class
        );
        return eBookRepository;
    }
}
//>>> DDD / Aggregate Root
