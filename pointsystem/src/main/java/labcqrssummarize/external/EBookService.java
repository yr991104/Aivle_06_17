package labcqrssummarize.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "aisystem", url = "${api.url.aisystem}")
public interface EBookService {
    
    @RequestMapping(method = RequestMethod.GET, path = "/eBooks/{ebookId}")
    public EBook getEBook(@PathVariable("ebookId") String ebookId);
} 