package labcqrssummarize.external;

import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "adminsystem", url = "${api.url.adminsystem}")
public interface EBookService {
    @RequestMapping(method = RequestMethod.POST, path = "/eBooks")
    public void examineRequestPublish(@RequestBody EBook eBook);
}
