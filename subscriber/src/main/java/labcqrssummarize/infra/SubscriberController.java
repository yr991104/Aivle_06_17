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
}
//>>> Clean Arch / Inbound Adaptor
