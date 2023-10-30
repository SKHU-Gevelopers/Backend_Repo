package site.unimeet.unimeetbackend.domain.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.student.dto.EditMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.MyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.PublicMyPageDto;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenManager;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookRepository;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthException;
import site.unimeet.unimeetbackend.util.EntityUtil;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final GuestBookRepository guestBookRepository;
    private final TokenManager tokenManager;

    public Student findById(Long id) {
        return EntityUtil.mustNotNull(
                studentRepository.findById(id), ErrorCode.STUDENT_NOT_FOUND);
    }

    public void checkEmailDuplicated(String email) {
        studentRepository.findByEmail(email)
                .ifPresent(user -> {throw new AuthException(ErrorCode.EMAIL_ALREADY_REGISTERED);}
                ); // throw는 statement lambda이며, expression lambda가 아니므로 중괄호 필요
    }

    @Transactional
    public void editMyPage(EditMyPageDto.Request editMyPageRequest, String uploadedFilePath , long id) {
        Student student = findById(id);
        editMyPageRequest.editMyPage(student, uploadedFilePath);
    }

    public MyPageDto.Rsp getMyPage(long id) {
        Student student = EntityUtil.mustNotNull(studentRepository.findByIdFetchMajors(id), ErrorCode.STUDENT_NOT_FOUND);
        return MyPageDto.Rsp.of(student);
    }

    public PublicMyPageDto.Res getPublicMyPage(Long id, Pageable pageable) {
        Student student = findById(id);
        Page<GuestBook> guestBooks = guestBookRepository.findByTargetStudent(student, pageable);
        return PublicMyPageDto.Res.from(student, guestBooks);
    }

    // refresh token으로 객체를 찾을 수 없으면 refresh token을 잊고 null을 반환한다.
    // 만료시간은 tokenManager.getMemberEmail(refresh)에서 검증했기 때문에, 추가 검증할 필요 없다.
    @Transactional
    public Student isValidRefreshToken(String refreshToken, long id) {
        Student student = studentRepository.findByRefreshToken(refreshToken);
        // refresh token으로 찾을 수 없다면, email로 찾아서 토큰을 잊는다.
        if (student == null) {
            student = findById(id);
            student.logout();
            return null;
        }
        return student;
    }

    @Transactional
    public void updateRefreshTokenAndExp(Student student, String refreshToken, Date refreshTokenExp) {
        Student savedStudent = studentRepository.save(student);// 영속화 후 update
        savedStudent.updateRefreshTokenAndExp(refreshToken, refreshTokenExp);
    }

    @Transactional
    public TokenDto oAuthSignIn(String kakaoIdTokenSub) {
        log.info("kakaoIdTokenSub: {}", kakaoIdTokenSub);
        Student student;
        boolean firstSignIn = false;
        if (studentRepository.existsByKakaoIdTokenSub(kakaoIdTokenSub)) {
            log.info("findByKakaoIdTokenSub called");
            student = studentRepository.findByKakaoIdTokenSub(kakaoIdTokenSub);
        } else {
            log.info("newStudent() called");
            student = newStudent(kakaoIdTokenSub);
            firstSignIn = true;
        }

        TokenDto tokenDto = tokenManager.createTokenDto(student.getId());

        // refresh token은 관리를 위해 user DB에 저장.
        student.updateRefreshTokenAndExp(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExp());

        tokenDto.setUsername(student.getNickname());
        tokenDto.setFirstSignIn(firstSignIn);
        return tokenDto;
    }

    private Student newStudent(String kakaoIdTokenSub) {
        // 닉네임과 같은 문자열 속성은 empty string으로 만들어 저장한다.
        // UUID 설정한 컬럼은 나중에 기능 안정화된 후 제거할 것임.
        Student newStudent = Student.builder()
                .profileImageUrl(S3Config.DEFAULT_PROFILE_IMAGE_URL)
                .mbti(Mbti.NONE)
                .nickname("")
                .gender(Gender.NONE)
                .kakaoIdTokenSub(kakaoIdTokenSub)

                .email(UUID.randomUUID().toString().substring(0, 10))
                .password(UUID.randomUUID().toString().substring(0, 10))
                .department(Department.NONE)
                .build();

        return studentRepository.save(newStudent);
    }
}









