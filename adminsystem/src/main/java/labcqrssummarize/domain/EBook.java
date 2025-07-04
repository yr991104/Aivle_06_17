package labcqrssummarize.domain;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import java.time.LocalDate;
import javax.persistence.*;
import labcqrssummarize.AdminsystemApplication;
import labcqrssummarize.domain.PublishCanceled;
import labcqrssummarize.domain.RequestContentApproved;
import labcqrssummarize.domain.RequestContentDenied;
import labcqrssummarize.domain.RequestPublishApproved;
import labcqrssummarize.domain.RequestPublishDenied;
import labcqrssummarize.domain.Switch2Private;
import labcqrssummarize.domain.PublicationStatus;


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
    private PublicationStatus publicationStatus; // 출간 상태 (PENDING, APPROVED, DENIED 등)

    @PrePersist
    public void onPostPersist() {
        RequestContentApproved event = new RequestContentApproved(this);
        event.publishAfterCommit();
    }

    /**
     * 콘텐츠 작성 완료 후 승인 처리
     * 승인 이벤트 발행
     */
    public void approveContent(String title, String content) {

        this.title = title;
        this.content = content;
        this.publicationStatus = PublicationStatus.CONTINUED;

        RequestContentApproved event = new RequestContentApproved(this);
        event.publishAfterCommit();
    }

    public void denyContent() {
        if (this.publicationStatus == PublicationStatus.CONTENTDENIED) {
            throw new IllegalStateException("이미 거부된 콘텐츠입니다.");
        }

        this.title = null;
        this.content = null;
        this.publicationStatus = PublicationStatus.CONTENTDENIED;

        RequestContentDenied event = new RequestContentDenied(this);
        event.publishAfterCommit();
    }
    /**
     * 출간 요청에 대한 승인 처리
     * 상태 변경 + 승인 이벤트 발행
     */
    public void approvePublish() {
        if (this.publicationStatus == PublicationStatus.PUBLICATIONAPPROVED) {
            throw new IllegalStateException("이미 출간 승인된 전자책입니다.");
        }
        this.publicationStatus = PublicationStatus.PUBLICATIONAPPROVED;

        RequestPublishApproved event = new RequestPublishApproved(this);
        event.publishAfterCommit();
    }

    /**
     * 출간 요청에 대한 거부 처리
     * 상태 변경 + 거부 이벤트 발행
     */
    public void denyPublish() {
        if (this.publicationStatus == PublicationStatus.PUBLICATIONDENIED) {
            throw new IllegalStateException("이미 출간 거부된 전자책입니다.");
        }
        this.publicationStatus = PublicationStatus.PUBLICATIONDENIED;

        RequestPublishDenied event = new RequestPublishDenied(this);
        event.publishAfterCommit();
    }
    public void switchToPrivate() {
        this.publicationStatus = PublicationStatus.PRIVATE;
        Switch2Private event = new Switch2Private(this);
        event.publishAfterCommit();
    }

    public void cancelPublish() {
        this.publicationStatus = PublicationStatus.PUBLICATIONCANCELED;
        PublishCanceled event = new PublishCanceled(this);
        event.publishAfterCommit();
    }


    /**
     * Repository 정적 접근 메서드
     */
    public static EBookRepository repository() {
        EBookRepository eBookRepository = AdminsystemApplication.applicationContext.getBean(
            EBookRepository.class
        );
        return eBookRepository;
    }
}
//>>> DDD / Aggregate Root
