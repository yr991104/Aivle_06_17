package labcqrssummarize.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import labcqrssummarize.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "userPoints",
    path = "userPoints"
)
public interface UserPointRepository
    extends PagingAndSortingRepository<UserPoint, String> {
    
    Optional<UserPoint> findByUserId(String userId);
}


