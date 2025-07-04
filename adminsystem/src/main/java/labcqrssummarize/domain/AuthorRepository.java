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
@RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository
    extends PagingAndSortingRepository<Author, String> {
        boolean existsByNameAndUserId(String name, String userId);
        Optional<Author> findByAuthorId(String authorId);
}
