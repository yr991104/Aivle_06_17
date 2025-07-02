package labcqrssummarize.domain;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import java.time.LocalDate;
import javax.persistence.*;
import labcqrssummarize.AdminsystemApplication;
import labcqrssummarize.domain.PublishCanceled;
import labcqrssummarize.domain.RequestContentApporved;
import labcqrssummarize.domain.RequestContentDenied;
import labcqrssummarize.domain.RequestPublishApproved;
import labcqrssummarize.domain.RequestPublishDenied;
import labcqrssummarize.domain.Switch2Private;
import labcqrssummarize.domain.PublicationStatus;
import labcqrssummarize.AdminsystemApplication;

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

    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus; // 출간 상태 (PENDING, APPROVED, DENIED 등)

    /**
     * 콘텐츠 작성 완료 후 승인 처리
     * 승인 이벤트 발행
     */
    public void approveContent() {
        RequestContentApporved event = new RequestContentApporved(this);
        event.publishAfterCommit();
    }
    public void denyContent() {
        // 거부 처리 로직
        RequestContentDenied event = new RequestContentDenied(this);
        event.publishAfterCommit();
    }
    /**
     * 출간 요청에 대한 승인 처리
     * 상태 변경 + 승인 이벤트 발행
     */
    public void approvePublish() {
        if (this.publicationStatus == PublicationStatus.APPROVED) {
            throw new IllegalStateException("이미 출간 승인된 전자책입니다.");
        }
        this.publicationStatus = PublicationStatus.APPROVED;

        RequestPublishApproved event = new RequestPublishApproved(this);
        event.publishAfterCommit();
    }

    /**
     * 출간 요청에 대한 거부 처리
     * 상태 변경 + 거부 이벤트 발행
     */
    public void denyPublish() {
        if (this.publicationStatus == PublicationStatus.DENIED) {
            throw new IllegalStateException("이미 출간 거부된 전자책입니다.");
        }
        this.publicationStatus = PublicationStatus.DENIED;

        RequestPublishDenied event = new RequestPublishDenied(this);
        event.publishAfterCommit();
    }
    public void switchToPrivate() {
        this.publicationStatus = PublicationStatus.PRIVATE;
        Switch2Private event = new Switch2Private(this);
        event.publishAfterCommit();
    }

    public void cancelPublish() {
        this.publicationStatus = PublicationStatus.CANCELED;
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
