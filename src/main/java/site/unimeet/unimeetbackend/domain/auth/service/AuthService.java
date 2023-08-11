package site.unimeet.unimeetbackend.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenManager;
import site.unimeet.unimeetbackend.domain.student.StudentService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {
    private final StudentService studentService;
    private final TokenManager tokenManager;

    // todo 재발급 토큰 관리
    public TokenDto signIn(String email, String password) { //로그인
        // 1. email, password로 검증
        studentService.validatePassword(email, password);

        // 2. 토큰 생성
        return tokenManager.createTokenDto(email);
    }
}