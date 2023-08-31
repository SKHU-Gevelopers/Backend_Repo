package site.unimeet.unimeetbackend.domain.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.api.student.dto.EditMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.MyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.PublicMyPageDto;
import site.unimeet.unimeetbackend.domain.auth.service.EmailVerificationService;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBook;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookRepository;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthenticationException;
import site.unimeet.unimeetbackend.util.EntityUtil;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final GuestBookRepository guestBookRepository;

    public Student findById(Long id) {
        return EntityUtil.mustNotNull(
                studentRepository.findById(id), ErrorCode.STUDENT_NOT_FOUND);
    }

    public void checkEmailDuplicated(String email) {
        studentRepository.findByEmail(email)
                .ifPresent(user -> {throw new AuthenticationException(ErrorCode.EMAIL_ALREADY_REGISTERED);}
                ); // throw는 statement lambda이며, expression lambda가 아니므로 중괄호 필요
    }

    public Student findByEmail(String email) {
        return EntityUtil.mustNotNull(studentRepository.findByEmail(email), ErrorCode.STUDENT_NOT_FOUND);
    }

    @Transactional
    public Student signUp(Student student, String emailVrfCode) {
        // Cache 에서 Email로 검증코드를 가져온다.
        String cacheVrfCode = emailVerificationService.getCodeExpirationCache()
                .getIfPresent(student.getEmail());
        // 검증코드 만료 시 cacheVrfCode 는 null이다.
        if (cacheVrfCode == null) {
            throw new AuthenticationException(ErrorCode.EMAIL_VERIFICATION_CODE_NOT_FOUND);
        }
        // 캐시의 검증코드가 not null 이므로, 입력된 검증코드와 일치하는지 확인
        if (! cacheVrfCode.equals(emailVrfCode)){
            throw new AuthenticationException(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCHED);
        }

        if (studentRepository.existsByKakaoId(student.getKakaoId())) {
            throw new BusinessException(ErrorCode.KAKAO_ID_ALREADY_REGISTERED);
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        // 회원가입
        try {
            return studentRepository.saveAndFlush(student);
        } catch (DataIntegrityViolationException e) { // email unique 제약조건 위반 시
            throw new BusinessException(ErrorCode.STUDENT_CONSTRAINT_ERROR);
        }
    }

    @Transactional
    public void editMyPage(EditMyPageDto.Request editMyPageRequest, String uploadedFilePath ,String email) {
        Student student = findByEmail(email);
        editMyPageRequest.editMyPage(student, uploadedFilePath);
    }

    public MyPageDto.Response getMyPage(String email) {
        Student student = EntityUtil.mustNotNull(studentRepository.findByEmailFetchMajors(email), ErrorCode.STUDENT_NOT_FOUND);
        return MyPageDto.Response.of(student);
    }

    public PublicMyPageDto.Res getPublicMyPage(Long id, Pageable pageable) {
        Student student = findById(id);
        Page<GuestBook> guestBooks = guestBookRepository.findByTargetStudent(student, pageable);
        return PublicMyPageDto.Res.from(student, guestBooks);
    }

    public Student findByRefreshToken(String refreshToken) {
        return EntityUtil.mustNotNull(studentRepository.findByRefreshToken(refreshToken), ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}