package labcqrssummarize.domain;

import java.util.Date;
import java.util.List;
import labcqrssummarize.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "subscribers",
    path = "subscribers"
)
public interface SubscriberRepository
    extends PagingAndSortingRepository<Subscriber, String> {
        //로그인 시 userId로 조회
        Optional<Subscriber> findByUserId(String userId);
    }
