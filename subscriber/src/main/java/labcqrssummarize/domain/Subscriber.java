package labcqrssummarize.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Optional;

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
    //회원가입
    public static Subscriber registerSubscriber(RegisterSubscriberCommand cmd) {
        // 중복 userId 체크 오류 메세지 추후 수정 예정
        Optional<Subscriber> existing = repository().findByUserId(cmd.getUserId());
        if (existing.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "이미 사용중인 id 입니다."
            );
        }
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

    /*로그인 메서드
     * 로그인 실패 시 message가 안떠서 추후 수정 예
     */
    public static Subscriber login(LoginCommand cmd) {
        Subscriber s = repository()
            .findByUserId(cmd.getUserId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "아이디가 올바르지 않습니다."
            ));
        if (!s.getPassword().equals(cmd.getPassword())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."
            );
        }
        new LoggedIn(s).publishAfterCommit();
        return s;
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
    //KT멤버쉽 신청
    public void requestMembership(RegisterMembershipCommand cmd) {
        this.membershipType     = MembershipType.KT;
        repository().save(this);
        new MembershipRequested(this).publishAfterCommit();
    }
    public void requestOpenEBook(RequestOpenEBookCommand cmd) {
        // 단순히 이벤트 발행
        new RequestOpenEBookAccept(this, cmd.getEbookId())
            .publishAfterCommit();
    }    

    public static SubscriberRepository repository() {
        return SubscriberApplication.applicationContext.getBean(SubscriberRepository.class);
    }
}


