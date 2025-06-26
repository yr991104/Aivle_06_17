package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.EbookplatformApplication;
import labcqrssummarize.domain.ListOutEbook;
import labcqrssummarize.domain.ListUpEbook;
import lombok.Data;

@Entity
@Table(name = "EBookPlatform_table")
@Data
//<<< DDD / Aggregate Root
public class EBookPlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ebookId;

    private String authorId;

    private String contentId;

    private String aiGeneratedCover;

    private String ebookStatus;

    private Date registeredAt;

    @PostPersist
    public void onPostPersist() {
        ListUpEbook listUpEbook = new ListUpEbook(this);
        listUpEbook.publishAfterCommit();

        ListOutEbook listOutEbook = new ListOutEbook(this);
        listOutEbook.publishAfterCommit();
    }

    public static EBookPlatformRepository repository() {
        EBookPlatformRepository eBookPlatformRepository = EbookplatformApplication.applicationContext.getBean(
            EBookPlatformRepository.class
        );
        return eBookPlatformRepository;
    }

    //<<< Clean Arch / Port Method
    public static void listOutEbook(RequestListOutEbook requestListOutEbook) {
        //implement business logic here:

        /** Example 1:  new item 
        EBookPlatform eBookPlatform = new EBookPlatform();
        repository().save(eBookPlatform);

        */

        /** Example 2:  finding and process
        

        repository().findById(requestListOutEbook.get???()).ifPresent(eBookPlatform->{
            
            eBookPlatform // do something
            repository().save(eBookPlatform);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
