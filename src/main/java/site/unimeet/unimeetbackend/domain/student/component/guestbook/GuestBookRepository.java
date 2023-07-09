package site.unimeet.unimeetbackend.domain.student.component.guestbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    @Query("Select g From GuestBook g Join Fetch g.writer Where g.targetStudent.id = :id")
    List<GuestBook> findByTargetStudentId(Long id);
}