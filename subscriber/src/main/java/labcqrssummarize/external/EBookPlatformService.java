package labcqrssummarize.external;

import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ebookplatform", url = "${api.url.ebookplatform}")
public interface EBookPlatformService {
    @RequestMapping(method = RequestMethod.POST, path = "/eBookPlatforms")
    public void openEBook(@RequestBody EBookPlatform eBookPlatform);
}
