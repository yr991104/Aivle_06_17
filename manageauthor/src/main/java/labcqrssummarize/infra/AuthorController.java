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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    // ===== DTO 클래스들 =====

    @Data
    @NoArgsConstructor
    static class RegisterAuthorRequest {
        private String name;
        private String userId;
    }

    @Data
    @NoArgsConstructor
    static class WriteContentRequest {
        private String title;
        private String content;
    }

    // ===== API 메서드들 =====
    // 전체 조회
    @GetMapping
    public List<Author> getAllAuthors() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }


    // 1. 작가 등록
    @PostMapping // 신규 작가를 등록하는 API이기 떄문에 authorId가 아직 없음(DB에 저장되기 전) 그래서 경로 없음
    public ResponseEntity<Void> registerAuthor(@RequestBody RegisterAuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setUserId(request.getUserId());
        author.setIsApproved(false);
        authorRepository.save(author); // 등록 시 @PostPersist로 RegisteredAuthor 발행
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 콘텐츠 작성
    @PostMapping("/{authorId}/write-content")
    public ResponseEntity<Void> writeContent(
            @PathVariable String authorId,
            @RequestBody WriteContentRequest request
    ) {
        Author author = authorRepository.findByAuthorId(authorId)
            .orElseThrow(() -> new RuntimeException("작가를 찾을 수 없습니다."));
        author.writeContent(request.getTitle(), request.getContent(), author.getAuthorId());
        return ResponseEntity.ok().build();
    }

    // 3. 출간 요청
    @PostMapping("/{authorId}/ebooks/{ebookId}/request-publish")
    public ResponseEntity<Void> requestPublish(
        @PathVariable String authorId,
        @PathVariable String ebookId) {

        Author author = authorRepository.findByAuthorId(authorId)
            .orElseThrow(() -> new RuntimeException("작가를 찾을 수 없습니다."));

        if (!author.getEbooks().contains(ebookId)) {
            throw new RuntimeException("해당 작가가 소유한 eBook이 아닙니다.");
        }

        author.requestPublish(ebookId);
        return ResponseEntity.ok().build();
    }


    // 4. 출간 취소
    @PatchMapping("/{authorId}/cancel-publish")
    public ResponseEntity<Void> cancelPublish(@PathVariable String authorId) {
        Author author = authorRepository.findByAuthorId(authorId)
                .orElseThrow(() -> new RuntimeException("작가를 찾을 수 없습니다."));
        author.cancelPublish();
        return ResponseEntity.ok().build();
    }

    // 5. 전자책 비공개
    @PatchMapping("/{authorId}/list-out")
    public ResponseEntity<Void> listOut(@PathVariable String authorId) {
        Author author = authorRepository.findByAuthorId(authorId)
                .orElseThrow(() -> new RuntimeException("작가를 찾을 수 없습니다."));
        author.listOutEbook();
        return ResponseEntity.ok().build();
    }
}
//>>> Clean Arch / Inbound Adaptor
