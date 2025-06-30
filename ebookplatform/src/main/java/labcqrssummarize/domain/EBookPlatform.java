package labcqrssummarize.domain;

import jakarta.persistence.*;
import labcqrssummarize.EbookplatformApplication;
import labcqrssummarize.domain.HandleEBookViewFailed;
import labcqrssummarize.domain.HandleEBookViewed;
import labcqrssummarize.domain.ListOutEBook;
import labcqrssummarize.domain.ListedUpEBook;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EBookPlatform_table")
@Data
public class EBookPlatform {

    public enum EbookStatus {
        IN_PROGRESS,
        READY_FOR_PUBLISH,
        OPENED,
        REMOVED
    }

    @Id
    private Integer pid;

    private String ebooks;

    private Date registeredAt;

    private String coverImage;

    private String summary;

    private Integer prices;

    @Enumerated(EnumType.STRING)
    private EbookStatus status = EbookStatus.IN_PROGRESS;

    private boolean coverGenerated = false;
    private boolean contentSummarized = false;
    private boolean priceAndCategorySet = false;

    // === 상태 전이 메서드 ===
    public void markCoverGenerated() {
        this.coverGenerated = true;
    }

    public void markContentSummarized() {
        this.contentSummarized = true;
    }

    public void markPriceAndCategorySet() {
        this.priceAndCategorySet = true;
    }

    public boolean isReadyForPublish() {
        return coverGenerated && contentSummarized && priceAndCategorySet;
    }

    public void updateStatus(EbookStatus newStatus) {
        this.status = newStatus;
    }

    public void register() {
        if (!isReadyForPublish()) {
            throw new IllegalStateException("아직 출판 준비가 완료되지 않았습니다.");
        }

        this.status = EbookStatus.READY_FOR_PUBLISH;
        this.registeredAt = LocalDateTime.now();

        EBookPlatformRegistered registeredEvent = new EBookPlatformRegistered(this);
        registeredEvent.publishAfterCommit();
    }

    public boolean openEbook() {
        if (this.status != EbookStatus.READY_FOR_PUBLISH) {
            return false;  // 열람 실패
        }

        this.status = EbookStatus.OPENED;

        EBookPlatformOpened openedEvent = new EBookPlatformOpened(this);
        openedEvent.publishAfterCommit();

        HandleEBookViewed handleEBookViewed = new HandleEBookViewed(this);
        handleEBookViewed.publishAfterCommit();

        HandleEBookViewFailed handleEBookViewFailed = new HandleEBookViewFailed(
            this
        );
        handleEBookViewFailed.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        ListOutEBook listOutEBook = new ListOutEBook(this);
        listOutEBook.publishAfterCommit();
    }

    // === Repository 접근 ===
    public static EBookPlatformRepository repository() {
        return EbookplatformApplication.applicationContext.getBean(EBookPlatformRepository.class);
    }
}