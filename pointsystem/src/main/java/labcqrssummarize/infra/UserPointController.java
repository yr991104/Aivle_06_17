package labcqrssummarize.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/userPoints")
@Transactional
public class UserPointController {

    @Autowired
    UserPointRepository userPointRepository;

    // 기존 포인트 차감 API
    @PostMapping("/deduct")
    public PointDeductionResponse deductPoint(@RequestBody PointDeductionRequest request) {
        return UserPoint.deductPoint(request);
    }

    // 포인트 조회용
    @GetMapping("/{userId}")
    public UserPoint getUserPoint(@PathVariable String userId) {
        return userPointRepository.findById(userId).orElse(null);
    }

    // Kafka 이벤트 DeductPoint 직접 발행 테스트용 API 추가
    @Autowired
    private StreamBridge streamBridge;

    @PostMapping("/deduct/event")
    public String sendDeductPointEvent(@RequestBody DeductPoint deductPoint) {
        streamBridge.send("event-out", deductPoint);
        return "DeductPoint 이벤트가 발송되었습니다.";
    }
}

//>>> Clean Arch / Inbound Adaptor
