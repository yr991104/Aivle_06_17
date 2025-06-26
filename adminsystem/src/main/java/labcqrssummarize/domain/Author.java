package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.AdminsystemApplication;
import labcqrssummarize.domain.RequestAsAuthorApproved;
import labcqrssummarize.domain.RequestAsAuthorDenied;
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

    private Books eBook;

    @PostPersist
    public void onPostPersist() {
        RequestAsAuthorApproved requestAsAuthorApproved = new RequestAsAuthorApproved(
            this
        );
        requestAsAuthorApproved.publishAfterCommit();

        RequestAsAuthorDenied requestAsAuthorDenied = new RequestAsAuthorDenied(
            this
        );
        requestAsAuthorDenied.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AdminsystemApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }
}
//>>> DDD / Aggregate Root
