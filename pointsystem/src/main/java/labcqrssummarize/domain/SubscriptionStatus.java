package labcqrssummarize.domain;

public enum SubscriptionStatus {
    NONE,         // 회원가입 직후 상태
    SUBSCRIBED,   // 구독 중
    CANCELED,     // 구독 취소
    EXPIRED       // 구독 만료
}