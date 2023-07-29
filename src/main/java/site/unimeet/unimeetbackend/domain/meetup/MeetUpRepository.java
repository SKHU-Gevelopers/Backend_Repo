package site.unimeet.unimeetbackend.domain.meetup;

import org.springframework.data.jpa.repository.JpaRepository;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;

public interface MeetUpRepository extends JpaRepository<MeetUp, Long> {
    List<MeetUp> findAllByReceiver(Student receiver);
}