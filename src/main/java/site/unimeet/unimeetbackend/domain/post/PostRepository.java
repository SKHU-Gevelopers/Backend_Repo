package site.unimeet.unimeetbackend.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long>{
    Optional<Post> findById(Long id);
}
