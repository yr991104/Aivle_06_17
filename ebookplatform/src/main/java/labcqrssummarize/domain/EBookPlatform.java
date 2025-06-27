package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.EbookplatformApplication;
import labcqrssummarize.domain.ListOutEBook;
import labcqrssummarize.domain.ListedUpEBook;
import lombok.Data;

@Entity
@Table(name = "EBookPlatform_table")
@Data
//<<< DDD / Aggregate Root
public class EBookPlatform {

    @Id
    private Integer pid;

    private String ebooks;

    private String aiGeneratedCover;

    private Date registeredAt;

    @PostPersist
    public void onPostPersist() {
        ListedUpEBook listedUpEBook = new ListedUpEBook(this);
        listedUpEBook.publishAfterCommit();

        ListOutEBook listOutEBook = new ListOutEBook(this);
        listOutEBook.publishAfterCommit();
    }

    public static EBookPlatformRepository repository() {
        EBookPlatformRepository eBookPlatformRepository = EbookplatformApplication.applicationContext.getBean(
            EBookPlatformRepository.class
        );
        return eBookPlatformRepository;
    }
}
//>>> DDD / Aggregate Root
