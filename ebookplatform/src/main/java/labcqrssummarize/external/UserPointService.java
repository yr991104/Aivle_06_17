package labcqrssummarize.external;

import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "pointsystem", url = "${api.url.pointsystem}")
public interface UserPointService {
    @RequestMapping(method = RequestMethod.PATCH, path = "/userPoints")
    public void reducePoint(@RequestBody UserPoint userPoint);
}
