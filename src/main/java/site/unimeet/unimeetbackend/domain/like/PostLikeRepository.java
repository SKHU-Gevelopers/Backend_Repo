package site.unimeet.unimeetbackend.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndStudent(@Param("post")Post post, Student student);

    //findAllIdFetchPostandEmail
}
