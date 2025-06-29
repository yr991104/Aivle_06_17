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
    //회원 가입 처리
    public static Subscriber registerSubscriber(RegisterSubscriberCommand cmd) {
        Subscriber subscriber = new Subscriber();

        subscriber.subscriberId     = UUID.randomUUID().toString();
        subscriber.userId           = cmd.getUserId();
        subscriber.password         = cmd.getPassword();
        subscriber.email            = cmd.getEmail();
        subscriber.subscriptionType = cmd.getSubscriptionType(); 

        repository().save(subscriber);
        new SignedUp(subscriber).publishAfterCommit();
        return subscriber;
    }

    //<<< Clean Arch / Port Method
    //구독 신청 메서드
    public void subscribe(RegisterSubscriptionCommand cmd) {
        this.subscriptionType   = cmd.getSubscriptionType();
        this.subscriptionStatus = SubscriptionStatus.SUBSCRIBED;
        this.startedAt          = LocalDateTime.now();
        this.expiredAt          = this.startedAt.plusMonths(
            this.subscriptionType == SubscriptionType.MONTHLY ? 1 : 12
        );

        repository().save(this);
        new SubscribeRequested(this).publishAfterCommit();
    }

    //<<< Clean Arch / Port Method
    //구독 취소 메서드 취소 시 즉시 취소(expiredAt에 현재 시간 표기)
     public void cancelSubscription() {
        this.subscriptionStatus = SubscriptionStatus.CANCELED;
        this.subscriptionType   = null;
        this.expiredAt          = LocalDateTime.now();
        repository().save(this);
        new SubscribeCanceled(this).publishAfterCommit();
    }

    public static SubscriberRepository repository() {
        return SubscriberApplication.applicationContext.getBean(SubscriberRepository.class);
    }
}


