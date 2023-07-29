package site.unimeet.unimeetbackend.domain.meetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;

public interface MeetUpRepository extends JpaRepository<MeetUp, Long> {
    List<MeetUp> findAllByReceiver(Student receiver);

    // receiver, sender, targetPost, imageUrls 모두 Fetch Join 한다. 쿼리 수정시 메서드명 변경 고려해야 함
    @Query("Select m From MeetUp m" +
            " Join Fetch m.receiver Join Fetch m.targetPost Join Fetch m.sender Left Join Fetch m.imageUrls" +
            " where m.id = :meetUpId")
    MeetUp findByIdFetchAll(Long meetUpId);

    @Query("Select m From MeetUp m" +
            " Join Fetch m.receiver Join Fetch m.targetPost" +
            " where m.id = :meetUpId")
    MeetUp findByIdFetchReceiverAndPost(Long meetUpId);
}