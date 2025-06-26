package labcqrssummarize.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import labcqrssummarize.SubscriberApplication;
import labcqrssummarize.domain.CancelSubscribe;
import labcqrssummarize.domain.RequestSubscribe;
import labcqrssummarize.domain.SignUp;
import lombok.Data;

@Entity
@Table(name = "Subscriber_table")
@Data
//<<< DDD / Aggregate Root
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String subscriberId;

    private String userId;

    private String subscriptionStatus;

    private String subscriptionType;

    private Date startedAt;

    private Date expiredAt;

    private ViewHistory viewHistory;

    private PointHistory pointHistory;

    @PostPersist
    public void onPostPersist() {
        SignUp signUp = new SignUp(this);
        signUp.publishAfterCommit();

        RequestSubscribe requestSubscribe = new RequestSubscribe(this);
        requestSubscribe.publishAfterCommit();

        CancelSubscribe cancelSubscribe = new CancelSubscribe(this);
        cancelSubscribe.publishAfterCommit();
    }

    public static SubscriberRepository repository() {
        SubscriberRepository subscriberRepository = SubscriberApplication.applicationContext.getBean(
            SubscriberRepository.class
        );
        return subscriberRepository;
    }

    //<<< Clean Arch / Port Method
    public static void recommandKtMembership(SignUp signUp) {
        //implement business logic here:

        /** Example 1:  new item 
        Subscriber subscriber = new Subscriber();
        repository().save(subscriber);

        */

        /** Example 2:  finding and process
        

        repository().findById(signUp.get???()).ifPresent(subscriber->{
            
            subscriber // do something
            repository().save(subscriber);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
