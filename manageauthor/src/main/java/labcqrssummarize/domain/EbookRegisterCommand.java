package labcqrssummarize.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 전자책 등록용 Command
 * - 콘텐츠 작성 이벤트(WrittenContent)로부터 생성됨
 * - 이후 출판 시스템에 전달되어 전자책으로 등록됨
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EbookRegisterCommand {
    private Long ebookId;
    private String authorId;   // 작성자 ID
    private String title;      // 전자책 제목
    private String content;    // 전자책 본문 내용
    private String category;   // 기본값: "기타"
    private int price;         // 기본값: 0

    // 요약은 추후 AI 컨텍스트에서 생성 예정 (summary 필드는 생략)
}
