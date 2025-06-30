package labcqrssummarize;

import labcqrssummarize.domain.EBook;
import labcqrssummarize.domain.EBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private EBookRepository ebookRepository;

    @PostMapping("/create")
    public EBook createEBook(@RequestBody EBook ebook) {
        return ebookRepository.save(ebook);
    }

    @GetMapping("/ebook/{id}")
    public EBook getEBook(@PathVariable("id") String id) {
        return ebookRepository.findById(id).orElse(null);
    }
}
