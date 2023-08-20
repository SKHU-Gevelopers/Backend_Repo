package site.unimeet.unimeetbackend.domain.student.component.guestbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.unimeet.unimeetbackend.domain.student.Student;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    @Query(value =
            "Select g From GuestBook g" +
            " Join Fetch g.writer" +
            " Where g.targetStudent = :targetStudent"
            , countQuery =
            "Select count(g) From GuestBook g" +
            " Where g.targetStudent = :targetStudent")
    Page<GuestBook> findByTargetStudent(@Param("targetStudent") Student targetStudent, Pageable pageable);
}