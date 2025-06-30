package labcqrssummarize.domain;

import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

/**
 * 전자책 열람 이벤트 (Kafka 또는 REST로 수신)
 * - userId: 사용자 ID
 * - ebookId: 전자책 ID
 * - price: 전자책 가격
 * - registeredAt: 열람 시각
 */
@Data
@ToString
public class HandleEBookViewed extends AbstractEvent {

    private String userId;    // 사용자 ID
    private String ebookId;   // 전자책 ID
    private Integer price;    // 전자책 가격
    private Date registeredAt;// 열람 시각
}
