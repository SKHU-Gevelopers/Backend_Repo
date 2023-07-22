package site.unimeet.unimeetbackend.domain.student.component.dm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;
import java.util.Optional;

public interface DMRepository extends JpaRepository<DM, Long> {
    @Query("Select dm From DM dm Join Fetch dm.sender Join Fetch dm.receiver where dm.id = :dmId")
    Optional<DM> findByIdFetchSenderReceiver(Long dmId);

    List<DM> findByReceiver(Student receiver);

    @Query("Select dm From DM dm Join Fetch dm.sender where dm.receiver = :receiver")
    List<DM> findAllByReceiverFetchSender(Student receiver);
}