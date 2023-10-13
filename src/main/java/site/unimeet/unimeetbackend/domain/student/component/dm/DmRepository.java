package site.unimeet.unimeetbackend.domain.student.component.dm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;
import java.util.Optional;

public interface DmRepository extends JpaRepository<Dm, Long> {
    @Query("Select dm From Dm dm Join Fetch dm.sender Join Fetch dm.receiver where dm.id = :dmId")
    Optional<Dm> findByIdFetchSenderReceiver(Long dmId);

    @Query("Select dm From Dm dm Join Fetch dm.sender where dm.receiver = :receiver")
    List<Dm> findAllByReceiverFetchSender(Student receiver);

    @Query("Select dm From Dm dm Join Fetch dm.receiver where dm.sender = :sender")
    List<Dm> findAllBySenderFetchReceiver(Student sender);
}