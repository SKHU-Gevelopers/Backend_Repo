package site.unimeet.unimeetbackend.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.auth.service.EmailVerificationService;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthenticationException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    @Transactional
    public User signUp(User user) {
        // Cache 에서 Email로 검증코드를 가져온다.
        String vrfCode = emailVerificationService.getCodeExpirationCache()
                .getIfPresent(user.getEmail());
        // 검증코드 만료 시 null이 반환된다. throw exception
        if (vrfCode == null) {
            throw new AuthenticationException(ErrorCode.EMAIL_VERIFICATION_CODE_NOT_FOUND);
        }

        // 회원가입
        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException e) { // email unique 제약조건 위반 시
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }
    }

    // 로그인 시 이메일과 비밀번호가 유효한지 체크,
    public void validatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("로그인 시도, email: {}, 이메일이 일치하지 않습니다.", email);
                    return new AuthenticationException(ErrorCode.MISMATCHED_SIGNIN_INFO);
                });
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // printf 스타일로 로그 출력,
            log.error("로그인 시도, email: {}, pwd: {}, 비밀번호가 일치하지 않습니다.", email, password);
            throw new AuthenticationException(ErrorCode.MISMATCHED_SIGNIN_INFO);
        }
    }

    public void checkEmailDuplicated(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {throw new AuthenticationException(ErrorCode.MISMATCHED_SIGNIN_INFO);}
                ); // throw는 statement lambda이며, expression lambda가 아니므로 중괄호 필요
    }
}