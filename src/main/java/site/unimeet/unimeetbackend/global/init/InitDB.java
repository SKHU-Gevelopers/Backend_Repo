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

        // convert to constructor to builder
        Student student = Student.builder()
                .name("박세희")
                .nickname("박세희Nickname")
                .email("eeee@email.com")
                .password(passwordEncoder.encode("pppp"))
                .gender(Gender.MALE)
                .mbti(Mbti.INTP)
                .kakaoId("chj6703")
                .profileImageUrl(S3Config.DEFAULT_PROFILE_IMAGE_URL)
                .majors(majors)
                .department(Department.IT)
                .build();

        Student student2 = Student.builder()
                .name("괴인")
                .nickname("괴인Nickname")
                .email("eeee1@email.com")
                .password(passwordEncoder.encode("pppp"))
                .gender(Gender.MALE)
                .mbti(Mbti.INTP)
                .kakaoId("kakaoId")
                .profileImageUrl(S3Config.DEFAULT_PROFILE_IMAGE_URL)
                .majors(majors)
                .department(Department.SOCIAL)
                .build();

        Student student3 = Student.builder()
                .name("이예술")
                .nickname("이예술Nickname")
                .email("eeee2@email.com")
                .password(passwordEncoder.encode("pppp"))
                .gender(Gender.MALE)
                .mbti(Mbti.INTP)
                .kakaoId("kimdonguk")
                .profileImageUrl(S3Config.DEFAULT_PROFILE_IMAGE_URL)
                .majors(majors)
                .department(Department.SOCIAL)
                .build();

        studentRepository.save(student);
        studentRepository.save(student2);
        studentRepository.save(student3);

        GuestBook guestBook1 = GuestBook.builder()
                .content("박세희가 박세희에게")
                .writer(student)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook1);

        GuestBook guestBook2 = GuestBook.builder()
                .content("괴인이 박세희에게")
                .writer(student2)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook2);

        GuestBook guestBook3 = GuestBook.builder()
                .content("괴인이 박세희에게2")
                .writer(student2)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook3);

        GuestBook guestBook4 = GuestBook.builder()
                .content("이예술이 박세희에게")
                .writer(student3)
                .targetStudent(student)
                .build();
        guestBookRepository.save(guestBook4);

        GuestBook guestBook5 = GuestBook.builder()
                .content("이예술이 이예술에게")
                .writer(student3)
                .targetStudent(student3)
                .build();
        guestBookRepository.save(guestBook5);

        GuestBook guestBook6 = GuestBook.builder()
                .content("이예술이 이예술에게2")
                .writer(student3)
                .targetStudent(student3)
                .build();
        guestBookRepository.save(guestBook6);

        ArrayList<GuestBook> guestBooks = new ArrayList<>();
        int guestBookCount = 20;
        for (int i = 1; i <= guestBookCount; i++) {
            GuestBook guestBookElement = GuestBook.builder()
                    .content("괴인이 박세희에게 " + i)
                    .writer(student2)
                    .targetStudent(student)
                    .build();
            guestBooks.add(guestBookElement);
        }
        guestBookRepository.saveAll(guestBooks);

        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .imageUrls(null)
                .maxPeople(4)
                .gender(Gender.MALE)
                .writer(student)
                .build();
        postRepository.save(post);

        Post post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .imageUrls(null)
                .maxPeople(4)
                .gender(Gender.MALE)
                .writer(student2)
                .build();
        postRepository.save(post2);

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

        ArrayList<Post> posts = new ArrayList<>();
        int postCount = 30;
        for (int i = 1; i <= postCount; i++) {
            Post postElement = Post.builder()
                    .title("제목 " + i)
                    .content("내용 " + i)
                    .imageUrls(null)
                    .maxPeople(4)
                    .gender(Gender.MALE)
                    .writer(student)
                    .build();
            posts.add(postElement);
        }
        postRepository.saveAll(posts);
    }
}
















