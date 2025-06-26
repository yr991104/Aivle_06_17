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
import labcqrssummarize.domain.RequestPublicationApproved;
import labcqrssummarize.domain.RequestPublicationDenied;
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

    private String contentId;

    private String coverImage;

    private String summary;

    private Boolean isPublicationApproved;

    private String publicationStatus;

    @PostPersist
    public void onPostPersist() {
        RequestContentApporved requestContentApporved = new RequestContentApporved(
            this
        );
        requestContentApporved.publishAfterCommit();

        RequestContentDenied requestContentDenied = new RequestContentDenied(
            this
        );
        requestContentDenied.publishAfterCommit();

        RequestPublicationApproved requestPublicationApproved = new RequestPublicationApproved(
            this
        );
        requestPublicationApproved.publishAfterCommit();

        RequestPublicationDenied requestPublicationDenied = new RequestPublicationDenied(
            this
        );
        requestPublicationDenied.publishAfterCommit();
    }

    public static EBookRepository repository() {
        EBookRepository eBookRepository = AdminsystemApplication.applicationContext.getBean(
            EBookRepository.class
        );
        return eBookRepository;
    }
}
//>>> DDD / Aggregate Root
