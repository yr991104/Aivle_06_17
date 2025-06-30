package labcqrssummarize.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value = "/authors")
@Transactional
public class AuthorController {

    // 요청 본문(JSON)으로 받을 DTO 클래스
    @Data
    @NoArgsConstructor
    static class RegisterAuthorRequest { // 작가 등록 요청 DTO
        private String name;
        private String userId;
        private String ebooks;
    }

    @Data
    @NoArgsConstructor
    static class WriteContentRequest {
        private String authorId;
        private String content;
    }

    @Data
    @NoArgsConstructor
    static class RequestPublishRequest {
        private String authorId;
    }

    @Data
    @NoArgsConstructor
    static class CancelPublishRequest {
        private String authorId;
    }

    @Autowired
    AuthorRepository authorRepository;

    @PostMapping("/authors")
    public ResponseEntity<Void> registerAuthor(@RequestBody RegisterAuthorRequest request) {
        // 1. Author 도메인 객체 생성
        Author author = new Author();
        author.setName(request.getName());
        author.setUserId(request.getUserId());
        author.setEbooks(request.getEbooks());
        author.setIsApproved(false); // 초기엔 미승인 상태

        // 2. DB 저장
        authorRepository.save(author);

        // 3. 이벤트 발행
        RegisteredAuthor event = new RegisteredAuthor(author);
        event.publish(); // kafka 발행

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/write-content")
    public ResponseEntity<Void> writeContent(@RequestBody WriteContentRequest request) {
        // 1. Author 조회
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrows(() -> new RuntimeException("해당 작가를 찾을 수 없습니다."));

        // 2. 콘텐츠 작성 이벤트 생성 및 발행
        WrittenContent event = new WrittenContent();
        event.setAuthorId(author.getAuthorId());
        event.setContent(request.getContent());

        event.publish();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/request-publish")
    public ResponseEntity<Void> requestPublish(@RequestBody RequestPublishRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("해당 작가를 찾을 수 없습니다."));

        RequestPublish event = new RequestPublish(author);
        event.setAuthorId(author.getAuthorId());
        event.setIsApproved(author.getIsApproved());

        event.publish();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel-publish")
    public ResponseEntity<Void> cancelPublish(@RequestBody CancelPublishRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("해당 작가를 찾을 수 없습니다."));

        RequestPublishCanceled event = new RequestPublishCanceled(author);
        event.setAuthorId(author.getAuthorId());
        event.setIsApproved(author.getIsApproved());

        event.publish();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/hide-ebook")
    public ResponseEntity<Void> hideEbook(@RequestParam String authorId) {
        // 1. Author 조회
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작가가 없습니다: " + authorId));

        // 2. 이벤트 객체 생성 및 발행
        ListOutEbookRequested event = new ListOutEbookRequested(author);
        event.publish();

        return ResponseEntity.ok().build();
    }
}
//>>> Clean Arch / Inbound Adaptor
