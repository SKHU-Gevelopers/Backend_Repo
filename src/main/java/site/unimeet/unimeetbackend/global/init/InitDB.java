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
import site.unimeet.unimeetbackend.domain.student.component.dm.Dm;
import site.unimeet.unimeetbackend.domain.student.component.dm.DmRepository;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final StudentRepository studentRepository;
    private final MeetUpRepository meetUpRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuestBookRepository guestBookRepository;
    private final DmRepository dmRepository;


    @Transactional
    @PostConstruct
    public void init() {
        ArrayList<Major> majors = new ArrayList<>();
        majors.add(Major.AI);
        majors.add(Major.ENGLISH);

        // convert to constructor to builder
        Student student1 = Student.builder()
                .name("임정연")
                .nickname("찡스")
                .email("eeee@email.com")
                .password(passwordEncoder.encode("pppp"))
                .gender(Gender.FEMALE)
                .mbti(Mbti.ESTP)
                .kakaoId("jeongkite927")
                .profileImageUrl("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/user_profile_img/madman.jpg")
                .majors(majors)
                .department(Department.IT)
                .build();

        Student student2 = Student.builder()
                .name("조성우")
                .nickname("안아줘요")
                .email("eeee1@email.com")
                .password(passwordEncoder.encode("pppp"))
                .gender(Gender.MALE)
                .mbti(Mbti.ISTP)
                .kakaoId("hugme123")
                .profileImageUrl("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/user_profile_img/Quokka.jfif")
                .majors(majors)
                .department(Department.IT)
                .build();

        Student student3 = Student.builder()
                .name("이예슬")
                .nickname("콩이언니")
                .email("eeee2@email.com")
                .password(passwordEncoder.encode("pppp"))
                .gender(Gender.FEMALE)
                .mbti(Mbti.ISTJ)
                .kakaoId("eslint456")
                .profileImageUrl("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/user_profile_img/memoji.jpg")
                .majors(majors)
                .department(Department.HUMANITIES)
                .build();

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        GuestBook guestBook1 = GuestBook.builder()
                .content("이분 완전 웃겨요 배꼽 빠짐")
                .writer(student1)
                .targetStudent(student2)
                .build();
        guestBookRepository.save(guestBook1);

        GuestBook guestBook2 = GuestBook.builder()
                .content("콩이 귀여워! 귀여운 강아지 보고싶으면 당장 만남신청~")
                .writer(student1)
                .targetStudent(student3)
                .build();
        guestBookRepository.save(guestBook2);

        GuestBook guestBook3 = GuestBook.builder()
                .content("실패 없는 음식들! 완전 맛집 잘 알!")
                .writer(student2)
                .targetStudent(student1)
                .build();
        guestBookRepository.save(guestBook3);

        GuestBook guestBook4 = GuestBook.builder()
                .content("취향이 비슷해서 시간 가는 줄 모르고 대화함~")
                .writer(student2)
                .targetStudent(student3)
                .build();
        guestBookRepository.save(guestBook4);

        GuestBook guestBook5 = GuestBook.builder()
                .content("대화하면 시간 가는 줄 모를만큼 재밌는 사람")
                .writer(student3)
                .targetStudent(student1)
                .build();
        guestBookRepository.save(guestBook5);

        GuestBook guestBook6 = GuestBook.builder()
                .content("만나서 시간 가는 줄 모르고 떠들었네요! 재밌당")
                .writer(student3)
                .targetStudent(student2)
                .build();
        guestBookRepository.save(guestBook6);

        Post post = Post.builder()
                .title("카페 투어해요!")
                .content("우리랑 오늘 핫한 카페 같이 갈 사람~")
                .imageUrls(List.of("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/post_img/post1.jpg"))
                .maxPeople(2)
                .gender(Gender.MALE)
                .writer(student1)
                .build();
        postRepository.save(post);

        Post post2 = Post.builder()
                .title("LP 바 좋아하시는 분?")
                .content("정말 가고싶은 LP바가 있는데 함께 갈 사람 구해요! 위치는 이태원~")
                .imageUrls(List.of("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/post_img/post2.png"))
                .maxPeople(1)
                .gender(Gender.FEMALE)
                .writer(student2)
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("강아지 보러 와")
                .content("애견카페에서 일 하는데 오늘 우리집 멍멍이도 온다! 귀여운 울 아가 보고싶으면 쪽지하자")
                .imageUrls(List.of("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/post_img/post3.jpg"))
                .maxPeople(5)
                .gender(Gender.NONE)
                .writer(student3)
                .build();
        postRepository.save(post3);

        Post post4 = Post.builder()
                .title("산산기어 팝업가자")
                .content("이번에 23FW 팝업 같이 갈 사람? 더현대에서 한대! 같이 갈 사람이 없어서 구해봐")
                .imageUrls(List.of("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/post_img/post4.jpg"))
                .maxPeople(2)
                .gender(Gender.NONE)
                .writer(student1)
                .build();
        postRepository.save(post4);

        Post post5 = Post.builder()
                .title("4대4 미팅")
                .content("9월 30일, 위치는 아직 안 정했어 우리 같이 단톡 파서 정해보자! 난 회대생이고 나머진 다른 학교야. 쪽지 줘")
                .imageUrls(List.of("https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/post_img/post5.jpg"))
                .maxPeople(4)
                .gender(Gender.FEMALE)
                .writer(student2)
                .build();
        postRepository.save(post5);


        MeetUp meetUp = MeetUp.builder()
                .title("4대4 미팅 신청")
                .content("안녕? 게시글 보고 쪽지해! 아래 우리 사진 첨부했어! 보고 맘에 들면 연락해!")
                .contact("010-1234-1234")
                .imageUrls(null)
                .targetPost(post5)
                .sender(student1)
                .receiver(student2)
                .build();
        meetUpRepository.save(meetUp);

        Dm dm1 = Dm.builder()
                .title("콩이 너무 귀여워")
                .content("오늘 재밌었어! 집엔 잘 들어갔어? 다음에 우리 미키도 보여줄게!")
                .sender(student1)
                .receiver(student3)
                .build();
        dmRepository.save(dm1);

        Dm dm2 = Dm.builder()
                .title("얼른 미키 보고싶다")
                .content("나도 오늘 너무 즐거웠어 집은 방금 막 왔당 ㅎㅎ 다음 약속이 너무 기대 돼")
                .sender(student3)
                .receiver(student1)
                .build();
        dmRepository.save(dm2);
    }
}
















