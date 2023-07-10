package site.unimeet.unimeetbackend.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long>{
    @Query("Select p From Post p Join Fetch p.imageUrls where p.id = :id")
    Optional<Post> findByIdFetchImageUrls(Long id);
}
