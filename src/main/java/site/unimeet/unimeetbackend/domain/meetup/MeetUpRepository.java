package site.unimeet.unimeetbackend.domain.meetup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetUpRepository extends JpaRepository<MeetUp, Long> {
}