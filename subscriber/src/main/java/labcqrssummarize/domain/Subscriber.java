package labcqrssummarize.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import labcqrssummarize.SubscriberApplication;
import lombok.Data;

@Entity
@Table(name = "Subscriber_table")
@Data
//<<< DDD / Aggregate Root
public class Subscriber {

    @Id
    private String subscriberId;

    private String userId;

    private String subscriptionType;

    private LocalDateTime startedAt;

    private LocalDateTime expiredAt;

    // 전자책 열람 기록 - 일대다 관계로 가정
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private List<ViewHistory> viewHistory;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    private String password;

    private String email;  // Email 별도 클래스 없이 String 타입으로 사용

    @PostPersist
    public void onPostPersist() {
        SignedUp signedUp = new SignedUp(this);
        signedUp.publishAfterCommit();

        SubscribeRequested subscribeRequested = new SubscribeRequested(this);
        subscribeRequested.publishAfterCommit();

        SubscribeCanceled subscribeCanceled = new SubscribeCanceled(this);
        subscribeCanceled.publishAfterCommit();

        MembershipRequested membershipRequested = new MembershipRequested(this);
        membershipRequested.publishAfterCommit();
    }

    public static SubscriberRepository repository() {
        SubscriberRepository subscriberRepository = SubscriberApplication.applicationContext.getBean(
            SubscriberRepository.class
        );
        return subscriberRepository;
    }

    //<<< Clean Arch / Port Method
    public static void recommandKtMembership(SignedUp signedUp) {
        // business logic here
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
