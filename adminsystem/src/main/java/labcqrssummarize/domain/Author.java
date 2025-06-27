package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.AdminsystemApplication;
import labcqrssummarize.domain.RequestAuthorApproved;
import labcqrssummarize.domain.RequestAuthorDenied;
import lombok.Data;

@Entity
@Table(name = "Author_table")
@Data
//<<< DDD / Aggregate Root
public class Author {

    @Id
    private String authorId;

    private String name;

    private Boolean isApproved;

    private String ebooks;

    private String userId;

    @PreUpdate
    public void onPreUpdate() {
        RequestAuthorApproved requestAuthorApproved = new RequestAuthorApproved(
            this
        );
        requestAuthorApproved.publishAfterCommit();

        RequestAuthorDenied requestAuthorDenied = new RequestAuthorDenied(this);
        requestAuthorDenied.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AdminsystemApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }
}
//>>> DDD / Aggregate Root
