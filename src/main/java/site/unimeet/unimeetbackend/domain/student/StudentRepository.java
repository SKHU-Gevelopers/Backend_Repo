package site.unimeet.unimeetbackend.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student,Long> {
    Optional<Student> findByEmail(String email);

    @Query("Select distinct s From Student s Join Fetch s.majors Where s.id = :id")
    Student findByIdFetchMajors(long id);

    boolean existsByKakaoId(String kakaoId);

    boolean existsByEmail(String email);

    Student findByRefreshToken(String refreshToken);
}
