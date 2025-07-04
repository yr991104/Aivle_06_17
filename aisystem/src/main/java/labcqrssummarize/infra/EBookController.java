package labcqrssummarize.infra;

import labcqrssummarize.domain.EBook;
import labcqrssummarize.domain.EBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eBooks")
@Transactional
public class EBookController {

    @Autowired
    private EBookRepository eBookRepository;

    // 1. 전자책 전체 목록 조회
    @GetMapping
    public List<EBook> getAllEBooks() {
        return (List<EBook>) eBookRepository.findAll();
    }

    // 2. 전자책 상세 조회
    @GetMapping("/{ebookId}")
    public ResponseEntity<EBook> getEBookById(@PathVariable String ebookId) {
        Optional<EBook> ebook = eBookRepository.findById(ebookId);
        return ebook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
