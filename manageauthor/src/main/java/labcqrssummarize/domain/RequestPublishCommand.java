package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;

@Data
public class RequestPublishCommand {

    private Long ebookId;
    private String authorId;
    private String title;                // 전자책 제목
    private String summary = "요약 없음"; // 기본 요약 값 설정
}
