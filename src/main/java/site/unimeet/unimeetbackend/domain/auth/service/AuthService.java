package site.unimeet.unimeetbackend.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenManager;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentRepository;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final StudentRepository studentRepository;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;
    private final StudentService studentService;

    // email이 문제인지 pwd가 문제인지 알려주면 안됨
    @Transactional
    public TokenDto signIn(String email, String password) {
        // 1. email, password로 검증
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("로그인 시도, email: {}, 이메일이 일치하지 않습니다.", email);
                    return new AuthException(ErrorCode.MISMATCHED_SIGNIN_INFO);
                });

        if (!passwordEncoder.matches(password, student.getPassword())) {
            log.error("로그인 시도, email: {}, pwd: {}, 비밀번호가 일치하지 않습니다.", email, password);
            throw new AuthException(ErrorCode.MISMATCHED_SIGNIN_INFO);
        }
        TokenDto tokenDto = tokenManager.createTokenDto(email);

        // refresh token은 관리를 위해 user DB에 저장.
        student.updateRefreshTokenAndExp(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExp());

        tokenDto.setUsername(student.getName());
        return tokenDto;
    }


    public TokenDto reissueByRefreshToken(String refreshToken, String email) {
        // Member 객체를 찾아온 후 토큰 검증
        Student student = studentService.isValidRefreshToken(refreshToken, email);
        if (student == null) {
            throw new AuthException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = tokenManager.createTokenDto(email);
        studentService.updateRefreshTokenAndExp(student, tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExp());

        tokenDto.setUsername(student.getName());
        return tokenDto;
    }

    @Transactional
    public void logout(String email) {
        Student student = studentService.findByEmail(email);
        student.logout();
    }
}


















