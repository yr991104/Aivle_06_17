package labcqrssummarize.domain;

import labcqrssummarize.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeductPoint extends AbstractEvent {
    private String userId;
    private int point;      // 차감할 포인트 금액
    private String ebookId; // 차감 대상 전자책 ID (참고용)
    
    public DeductPoint() {
        super();
    }
}
