package site.unimeet.unimeetbackend.global.init;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuestBookRepository guestBookRepository;

    @Value("${default.profile.image.url}")
    private String defaultProfileImageUrl;

    @Transactional
    @PostConstruct
    public void init() {
        ArrayList<Major> majors = new ArrayList<>();
        majors.add(Major.AI);
        majors.add(Major.ENGLISH);
        Student student = new Student("이병건", "침착맨", "eeee@email.com", passwordEncoder.encode("pppp"),
                Gender.MALE, Mbti.INTP, defaultProfileImageUrl, majors, Department.IT);

        Student student2 = new Student("김경민", "경민이", "eeee1@email.com", passwordEncoder.encode("pppp"),
                Gender.FEMALE, Mbti.ENFP, defaultProfileImageUrl, majors, Department.SOCIAL);
        studentRepository.save(student);
        studentRepository.save(student2);

        GuestBook guestBook1 = GuestBook.builder()
                .content("이병건은 이병건을 좋아해요")
                .writer(student)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook1);

        GuestBook guestBook2 = GuestBook.builder()
                .content("김경민이 이병건을 좋아해요1")
                .writer(student2)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook2);

        GuestBook guestBook3 = GuestBook.builder()
                .content("김경민이 이병건을 좋아해요2")
                .writer(student2)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook3);

    }
}
