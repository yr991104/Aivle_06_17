package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.ManageauthorApplication;
import labcqrssummarize.domain.ContentWritten;
import labcqrssummarize.domain.RegisteredAuthor;
import labcqrssummarize.domain.RequestListOutEbook;
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
        RegisteredAuthor registeredAuthor = new RegisteredAuthor(this);
        registeredAuthor.publishAfterCommit();

        ContentWritten contentWritten = new ContentWritten(this);
        contentWritten.publishAfterCommit();

        RequestListOutEbook requestListOutEbook = new RequestListOutEbook(this);
        requestListOutEbook.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = ManageauthorApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }
}
//>>> DDD / Aggregate Root
