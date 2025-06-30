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

@Entity
@Table(name = "Author_table")
@Data
//<<< DDD / Aggregate Root
public class Author {

    @Id
    private String authorId;    // 작가 고유 ID (PK)

    private String name;        // 작가 이름
    private Boolean isApproved; // 관리자 승인 여부
    private String ebooks;      // 등록된 전자책 ID들
    private String userId;      // 작가 계정의 유저 ID

    // 1. 작가 등록 시점에 실행될 이벤트 발행
    @PostPersist
    public void onPostPersist() {
        RegisteredAuthor registeredAuthor = new RegisteredAuthor(this);
        registeredAuthor.publishAfterCommit();
    }

    // 2. 전자책 비공개 요청 시 수정(update) 이벤트 발행
    @PreUpdate
    public void onPreUpdate() {
        ListOutEbookRequested listOutEbookRequested = new ListOutEbookRequested(this);
        listOutEbookRequested.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        return ManageauthorApplication.applicationContext.getBean(AuthorRepository.class);
    }
}
//>>> DDD / Aggregate Root
