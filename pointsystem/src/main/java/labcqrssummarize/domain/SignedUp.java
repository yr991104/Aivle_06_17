package labcqrssummarize.domain;

import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

/**
 * 회원가입 이벤트 (Kafka로 수신)
 * - userId: 사용자 ID
 * - membershipType: KT/NORMAL 등 멤버십 타입
 * - 기타 가입 정보
 */
@Data
@ToString
public class SignedUp extends AbstractEvent {

    private String userId;           // 사용자 ID
    private String subscriptionType; // 구독 타입
    private Object viewHistory;      // 뷰 히스토리
    private String membershipType;   // KT, NORMAL 등 멤버십 타입
    private Object subscriptionStatus; // 구독 상태
    private String password;         // 비밀번호
    private Object email;            // 이메일
    private String subscriberId;     // 구독자 ID
}
