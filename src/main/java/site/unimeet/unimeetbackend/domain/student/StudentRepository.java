package site.unimeet.unimeetbackend.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student,Long> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByEmailAndPassword(String email, String password);

    @Query("Select distinct s From Student s Join Fetch s.majors Where s.email = :email")
    Student findByEmailFetchMajors(String email);
}
