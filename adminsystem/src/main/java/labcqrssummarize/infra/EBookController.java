package labcqrssummarize.infra;

import javax.transaction.Transactional;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor
// 관리자 시스템에서 전자책 관련 관리 기능을 제공하는 컨트롤러
// - 출간 승인/거부
// - 콘텐츠 승인/거부
// - 출간 취소
// - 전자책 비공개 처리 등 Aggregate 상태 변화를 직접 트리거
// ※ 실제 비즈니스 로직은 Aggregate(EBook) 내부 메서드를 통해만 처리됨
@RestController
@RequestMapping("/ebooks")
@Transactional
public class EBookController {

    @Autowired
    private EBookRepository eBookRepository;

    /**
     * 콘텐츠 승인 API
     * - 해당 전자책의 콘텐츠를 승인 처리
     */
    @PatchMapping("/{id}/approve-content")
    public void approveContent(@PathVariable String id) {
        EBook ebook = eBookRepository.findById(id).orElseThrow();
        ebook.approveContent();
        eBookRepository.save(ebook);
    }

    /**
     * 콘텐츠 거부 API
     * - 해당 전자책의 콘텐츠를 거부 처리
     */
    @PatchMapping("/{id}/deny-content")
    public void denyContent(@PathVariable String id) {
        EBook ebook = eBookRepository.findById(id).orElseThrow();
        ebook.denyContent();
        eBookRepository.save(ebook);
    }

    /**
     * 출간 승인 API
     * - 해당 전자책의 출간을 승인 처리
     */
    @PatchMapping("/{id}/approve-publish")
    public void approvePublish(@PathVariable String id) {
        EBook ebook = eBookRepository.findById(id).orElseThrow();
        ebook.approvePublish();
        eBookRepository.save(ebook);
    }

    /**
     * 출간 거부 API
     * - 해당 전자책의 출간을 거부 처리
     */
    @PatchMapping("/{id}/deny-publish")
    public void denyPublish(@PathVariable String id) {
        EBook ebook = eBookRepository.findById(id).orElseThrow();
        ebook.denyPublish();
        eBookRepository.save(ebook);
    }

    /**
     * 전자책 비공개 처리 API
     * - 해당 전자책을 비공개 상태로 전환
     */
    @PatchMapping("/{id}/switch-private")
    public void switchToPrivate(@PathVariable String id) {
        EBook ebook = eBookRepository.findById(id).orElseThrow();
        ebook.switchToPrivate();
        eBookRepository.save(ebook);
    }

    /**
     * 출간 취소 API
     * - 해당 전자책의 출간을 취소 처리
     */
    @PatchMapping("/{id}/cancel-publish")
    public void cancelPublish(@PathVariable String id) {
        EBook ebook = eBookRepository.findById(id).orElseThrow();
        ebook.cancelPublish();
        eBookRepository.save(ebook);
    }
}
//>>> Clean Arch / Inbound Adaptor
