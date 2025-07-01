package labcqrssummarize.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/userPoints")
@Transactional
public class UserPointController {

    @Autowired
    UserPointRepository userPointRepository;

    @PostMapping("/deduct")
    public PointDeductionResponse deductPoint(@RequestBody PointDeductionRequest request) {
        return UserPoint.deductPoint(request);
    }

    // 포인트 지급 조회용
    @GetMapping("/{userId}")
    public UserPoint getUserPoint(@PathVariable String userId) {
        return userPointRepository.findById(userId).orElse(null);
    }
}

//>>> Clean Arch / Inbound Adaptor
