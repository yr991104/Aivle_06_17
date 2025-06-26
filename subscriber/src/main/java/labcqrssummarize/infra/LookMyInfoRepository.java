package labcqrssummarize.infra;

import java.util.List;
import labcqrssummarize.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "lookMyInfos",
    path = "lookMyInfos"
)
public interface LookMyInfoRepository
    extends PagingAndSortingRepository<LookMyInfo, Long> {}
