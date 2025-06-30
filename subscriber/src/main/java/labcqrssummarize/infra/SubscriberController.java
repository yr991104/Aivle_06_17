package labcqrssummarize.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import labcqrssummarize.domain.RegisterSubscriberCommand;
import labcqrssummarize.domain.RegisterSubscriptionCommand;
import labcqrssummarize.domain.Subscriber;
import labcqrssummarize.domain.SubscriberRepository;
import labcqrssummarize.domain.RegisterMembershipCommand;
import labcqrssummarize.domain.LoginCommand;
import labcqrssummarize.domain.RequestOpenEBookCommand;

@RestController
@RequestMapping("/subscribers")
@Transactional
public class SubscriberController {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @PostMapping("/register")
    public ResponseEntity<Subscriber> registerSubscriber(
            @RequestBody RegisterSubscriberCommand cmd
    ) {
        Subscriber created = Subscriber.registerSubscriber(cmd);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(created);
    }
    @PostMapping("/login")
    public ResponseEntity<Subscriber> login(
        @RequestBody LoginCommand cmd
    ){
        Subscriber loggedIn= Subscriber.login(cmd);
        return ResponseEntity.ok(loggedIn);
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Subscriber> subscribe(
            @PathVariable("id") String id,
            @RequestBody RegisterSubscriptionCommand cmd
    ) {
        Subscriber subscriber = subscriberRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subscriber.subscribe(cmd);
        return ResponseEntity.ok(subscriber);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Subscriber> cancel(
            @PathVariable("id") String id
    ) {
        Subscriber s = subscriberRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        s.cancelSubscription();
        return ResponseEntity.ok(s);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscriber> getSubscriber(@PathVariable String id) {
        return subscriberRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{id}/membership")
    public ResponseEntity<Subscriber> requestMembership(
            @PathVariable("id") String id
    ) {
        Subscriber s = subscriberRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        s.requestMembership(new RegisterMembershipCommand());
        return ResponseEntity.ok(s);
    }

    @PostMapping("/{id}/open")
    public ResponseEntity<Void> requestOpenEBook(
            @PathVariable String id,
            @RequestBody RequestOpenEBookCommand cmd
    ) {
        Subscriber s = subscriberRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 명시적으로 ID/상태 보강
        cmd.setSubscriberId(id);
        cmd.setUserId(s.getUserId());
        cmd.setSubscriptionStatus(s.getSubscriptionStatus());

        s.requestOpenEBook(cmd);
        return ResponseEntity.ok().build();
    }    
    
}
//>>> Clean Arch / Inbound Adaptor
