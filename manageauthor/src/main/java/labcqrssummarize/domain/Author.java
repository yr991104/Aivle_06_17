package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.ManageauthorApplication;
import labcqrssummarize.domain.ListOutEbookRequested;
import labcqrssummarize.domain.RegisteredAuthor;
import labcqrssummarize.domain.RequestPublish;
import labcqrssummarize.domain.RequestPublishCanceled;
import labcqrssummarize.domain.WrittenContent;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;

@Entity
@Table(name = "Author_table")
@Data
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String authorId;     // 작가 식별 ID
    private String name;         // 작가 이름
    private Boolean isApproved;  // 관리자 승인 여부
    private String userId;       // 작가 유저 ID

    @ElementCollection
    @CollectionTable(name = "author_ebooks", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "ebooks")
    private List<String> ebooks = new ArrayList<>();

    // 작가 등록 이벤트 자동 발행
    @PostPersist
    public void onPostPersist() {
        RegisteredAuthor event = new RegisteredAuthor(this);
        event.publishAfterCommit();
    }

    // 수동 이벤트 발행 메서드들 (이벤트만 발행하고 DB 저장은 안 함)
    public void writeContent(String title, String content, String authorId) {
        WrittenContent event = new WrittenContent(this);
        event.setAuthorId(authorId);
        event.setTitle(title);
        event.setContent(content);
        event.publishAfterCommit();
    }

    public void requestPublish(String ebookId) {
        if (!this.ebooks.contains(ebookId)) {
            throw new IllegalArgumentException("작가가 소유하지 않은 eBook입니다.");
        }
        RequestPublish event = new RequestPublish(this, ebookId);
        event.publishAfterCommit();
    }


    public void cancelPublish() {
        RequestPublishCanceled event = new RequestPublishCanceled(this);
        event.publishAfterCommit();
    }

    public void listOutEbook() {
        ListOutEbookRequested event = new ListOutEbookRequested(this);
        event.publishAfterCommit();
    }

    // Repository 접근 (Context 통해 주입받는 방식 그대로 유지)
    public static AuthorRepository repository() {
        return ManageauthorApplication.applicationContext.getBean(AuthorRepository.class);
    }
}
//>>> DDD / Aggregate Root
