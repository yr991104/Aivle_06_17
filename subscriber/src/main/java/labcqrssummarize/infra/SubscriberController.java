package labcqrssummarize.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import labcqrssummarize.domain.RegisterSubscriberCommand;
import labcqrssummarize.domain.Subscriber;
import labcqrssummarize.domain.SubscriberRepository;

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
        // 테스트용 리턴 전체 바디
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscriber> getSubscriber(@PathVariable String id) {
        return subscriberRepository.findById(id)
            .map(sub -> ResponseEntity.ok(sub))
            .orElse(ResponseEntity.notFound().build());
    }
}


//>>> Clean Arch / Inbound Adaptor
