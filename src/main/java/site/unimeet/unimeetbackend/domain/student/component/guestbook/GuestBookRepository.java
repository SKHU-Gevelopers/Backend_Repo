package site.unimeet.unimeetbackend.domain.student.component.guestbook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

}