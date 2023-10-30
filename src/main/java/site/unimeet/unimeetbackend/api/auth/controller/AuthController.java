package site.unimeet.unimeetbackend.api.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.unimeet.unimeetbackend.api.auth.dto.UserSignInDto;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.domain.auth.service.AuthService;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenManager;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenValidator;
import site.unimeet.unimeetbackend.global.resolver.StudentId;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final TokenValidator tokenValidator;
    private final TokenManager tokenManager;

    // 로그인
    @PostMapping("/auth/sign-in")
    public ResTemplate<TokenDto> handleSignIn(@RequestBody @Valid UserSignInDto.Request singInRequest){
        TokenDto tokenDto = authService.signIn(singInRequest.getEmail(), singInRequest.getPassword());
        return new ResTemplate<>(HttpStatus.OK, "로그인 성공",tokenDto);
    }

    // 로그아웃
    @PostMapping("/sign-out")
    public ResTemplate<Void> logout(@StudentId long loggedInId) {
        // 액세스 토큰 검증은 필터에서 거치므로 바로 로그아웃 처리
        authService.logout(loggedInId);

        return new ResTemplate<>(HttpStatus.OK, "로그아웃 성공");
    }

    /**
     *   refresh 토큰을 이용, access 토큰을 재발급하는 메소드
     */
    @PostMapping(value = "/token/reissue")
    public ResTemplate<TokenDto> accessToken(HttpServletRequest httpServletRequest){
        log.info("토큰 재발급 요청");
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        tokenValidator.validateBearer(authorizationHeader);

        String refreshToken = authorizationHeader.split(" ")[1];
        long studentId = tokenManager.getStudentId(refreshToken);

        TokenDto tokenDto = authService.reissueByRefreshToken(refreshToken, studentId);

        return new ResTemplate<>(HttpStatus.OK, "토큰 재발급", tokenDto);
    }
}