package site.unimeet.unimeetbackend.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public User signUp(User user) {
        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException e) { // email unique 제약조건 위반 시
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }
    }

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
}