package labcqrssummarize.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class EBookPlatformController {

    @Autowired
    EBookPlatformRepository eBookPlatformRepository;

    // 전자책 등록 요청
    @PostMapping("/ebooks/register/{pid}")
    public String register(@PathVariable Integer pid) {
        Optional<EBookPlatform> optionalEbook = eBookPlatformRepository.findById(pid);

        if (optionalEbook.isEmpty()) {
            return "등록 실패: 해당 pid의 전자책이 존재하지 않습니다.";
        }

        EBookPlatform ebook = optionalEbook.get();
        try {
            ebook.register();
            eBookPlatformRepository.save(ebook);
            return "전자책이 등록되었습니다.";
        } catch (IllegalStateException e) {
            return "등록 실패: " + e.getMessage();
        }
    }

    // 전자책 열람 요청
    @GetMapping("/ebooks/open/{pid}")
    public String open(@PathVariable Integer pid) {
        Optional<EBookPlatform> optionalEbook = eBookPlatformRepository.findById(pid);

        if (optionalEbook.isEmpty()) {
            return "열람 실패: 해당 pid의 전자책이 존재하지 않습니다.";
        }

        EBookPlatform ebook = optionalEbook.get();
        boolean result = ebook.openEbook();
        eBookPlatformRepository.save(ebook);

        return result ? "전자책 열람 성공!" : "전자책 열람 실패: 아직 등록되지 않았습니다.";
    }

    // 전자책 비공개 요청
    @DeleteMapping("/ebooks/remove/{pid}")
    public String remove(@PathVariable Integer pid) {
        Optional<EBookPlatform> optionalEbook = eBookPlatformRepository.findById(pid);

        if (optionalEbook.isEmpty()) {
            return "비공개 실패: 해당 pid의 전자책이 존재하지 않습니다.";
        }

        EBookPlatform ebook = optionalEbook.get();
        ebook.updateStatus(EBookPlatform.EbookStatus.REMOVED);
        eBookPlatformRepository.delete(ebook);

        return "전자책이 비공개(삭제) 처리되었습니다.";
    }

    // 전자책 상태 조회
    @GetMapping("/ebooks/status/{pid}")
    public String checkStatus(@PathVariable Integer pid) {
        Optional<EBookPlatform> optionalEbook = eBookPlatformRepository.findById(pid);

        if (optionalEbook.isEmpty()) {
            return "조회 실패: 해당 pid의 전자책이 존재하지 않습니다.";
        }

        EBookPlatform ebook = optionalEbook.get();
        return "전자책 상태: " + ebook.getStatus();
    }
}