package site.unimeet.unimeetbackend.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenManager;
import site.unimeet.unimeetbackend.domain.user.UserService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {
    private final UserService userService;
    private final TokenManager tokenManager;
    // todo 재발급 토큰 관리
    public TokenDto signIn(String email, String password) {
        // 1. email, password로 검증
        userService.validatePassword(email, password);

        // 2. 토큰 생성
        return tokenManager.createTokenDto(email);
    }
}