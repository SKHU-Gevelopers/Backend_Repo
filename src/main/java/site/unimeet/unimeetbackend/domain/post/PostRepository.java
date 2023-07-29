package site.unimeet.unimeetbackend.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository  extends JpaRepository<Post, Long>{
    @Query("Select p From Post p Left Join Fetch p.imageUrls where p.id = :id")
    Post findByIdFetchImageUrls(Long id);

    @Query("Select p From Post p Join Fetch p.writer where p.id = :id")
    Post findByIdFetchWriter(Long id);
}
