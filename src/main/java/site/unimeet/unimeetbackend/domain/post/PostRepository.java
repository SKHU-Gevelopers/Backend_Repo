package site.unimeet.unimeetbackend.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    @Query("Select p From Post p" +
            " Join Fetch p.writer" +
            " Left Join Fetch p.imageUrls" +
            " where p.id = :id")
    Post findByIdFetchWriterAndImageUrls(Long id);

    @Query("Select p From Post p Join Fetch p.writer where p.id = :id")
    Post findByIdFetchWriter(Long id);

    @Query(value = "SELECT p FROM Post p" +
            " JOIN FETCH p.writer" +
            " ORDER BY p.id DESC"
            , countQuery = "SELECT count(p) FROM Post p")
    Slice<Post> findAllFetchWriter(Pageable pageable);


    // 페이지네이션 임시로 해제
    @Query(value = "SELECT p FROM Post p" +
            " JOIN FETCH p.writer" +
            " ORDER BY p.id DESC")
    List<Post> findAllFetchWriter();
}


