package labcqrssummarize.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EbookRepository extends JpaRepository<Ebook, Long> {
    // 필요한 경우 커스텀 쿼리 작성
}
