package site.unimeet.unimeetbackend.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostAndStudent(Post post, Student student);
}
