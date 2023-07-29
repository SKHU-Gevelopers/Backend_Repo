package site.unimeet.unimeetbackend.global.init;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.meetup.MeetUp;
import site.unimeet.unimeetbackend.domain.meetup.MeetUpRepository;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.PostRepository;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookRepository;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final StudentRepository studentRepository;
    private final MeetUpRepository meetUpRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuestBookRepository guestBookRepository;


    @Transactional
    @PostConstruct
    public void init() {
        ArrayList<Major> majors = new ArrayList<>();
        majors.add(Major.AI);
        majors.add(Major.ENGLISH);
        Student student = new Student("이병건", "침착맨", "eeee@email.com", passwordEncoder.encode("pppp"),
                Gender.MALE, Mbti.INTP, S3Config.DEFAULT_PROFILE_IMAGE_URL, majors, Department.IT);

        Student student2 = new Student("김경민", "경민이", "eeee1@email.com", passwordEncoder.encode("pppp"),
                Gender.FEMALE, Mbti.ENFP, S3Config.DEFAULT_PROFILE_IMAGE_URL, majors, Department.SOCIAL);

        Student student3 = new Student("감경민", "갱민이", "eeee2@email.com", passwordEncoder.encode("pppp"),
                Gender.FEMALE, Mbti.ENFJ, S3Config.DEFAULT_PROFILE_IMAGE_URL, majors, Department.HUMANITIES);
        studentRepository.save(student);
        studentRepository.save(student2);
        studentRepository.save(student3);

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

        GuestBook guestBook4 = GuestBook.builder()
                .content("김경민이 이병건을 좋아해요3")
                .writer(student3)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook4);

        GuestBook guestBook5 = GuestBook.builder()
                .content("김경민이 이병건을 좋아해요4")
                .writer(student3)
                .targetStudent(student3)
                .build();
        guestBookRepository.save(guestBook5);

        GuestBook guestBook6 = GuestBook.builder()
                .content("김경민이 이병건을 좋아해요5")
                .writer(student3)
                .targetStudent(student3)
                .build();
        guestBookRepository.save(guestBook6);

        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .imageUrls(null)
                .maxPeople(4)
                .gender(Gender.MALE)
                .writer(student)
                .build();
        postRepository.save(post);

        MeetUp meetUp = MeetUp.builder()
                .title("만남신청1")
                .content("만남신청내용1")
                .contact("010-1234-1234")
                .imageUrls(null)
                .targetPost(post)
                .sender(student2)
                .receiver(student)
                .build();
        meetUpRepository.save(meetUp);
    }
}
















