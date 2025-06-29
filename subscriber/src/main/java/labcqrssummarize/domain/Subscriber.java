package labcqrssummarize.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import labcqrssummarize.SubscriberApplication;
import lombok.Data;

//<<< DDD / Aggregate Root
@Entity
@Table(name = "Subscriber_table")
@Data
public class Subscriber {

    @Id
    private String subscriberId;

    private String userId;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType = MembershipType.NORMAL;

    /**
     * NONE: 가입만 된 상태
     * SUBSCRIBED: 실제 구독 진행된 상태
     * CANCELED: 구독 취소된 상태
     * EXPIRED: 구독 기간 만료된 상태
     */
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.NONE;

    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;

    @ElementCollection
    @CollectionTable(
        name = "Subscriber_ViewHistory",
        joinColumns = @JoinColumn(name = "subscriber_id")
    )
    private List<ViewHistory> viewHistory = new ArrayList<>();

    protected Subscriber() {}

    //<<< Clean Arch / Port Method

    public static Subscriber registerSubscriber(RegisterSubscriberCommand cmd) {
        Subscriber subscriber = new Subscriber();

        // 1) 구독자 식별 ID 자동생성
        subscriber.subscriberId = UUID.randomUUID().toString();

        //사용자 입력에 따라 저장
        subscriber.userId = cmd.getUserId();
        subscriber.password = cmd.getPassword();
        subscriber.email = cmd.getEmail();

        // 구독 유형 기본 NONE
        subscriber.subscriptionType = cmd.getSubscriptionType();

        // 멤버쉽 기본 NORMAL
        if (cmd.getMembershipType() != null) {
            subscriber.membershipType = cmd.getMembershipType();
        }

       
        repository().save(subscriber);
        new SignedUp(subscriber).publishAfterCommit();

        // 테스트 확인용 리턴
        return subscriber;
    }

    //<<< Clean Arch / Port Method
    public void cancelSubscription() {
        this.subscriptionStatus = SubscriptionStatus.CANCELED;
        repository().save(this);
        new SubscribeCanceled(this).publishAfterCommit();
    }

    //KT 고객 추천 미완성
    public static void recommandKtMembership(SignedUp event) {
        if (event.getMembershipType() == MembershipType.KT) {
            System.out.println("KT 고객에게 추천 메시지를 전송합니다: " + event.getUserId());
        }
    }

    public static SubscriberRepository repository() {
        return SubscriberApplication.applicationContext.getBean(SubscriberRepository.class);
    }
}



