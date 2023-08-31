package site.unimeet.unimeetbackend.api.auth.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.unimeet.unimeetbackend.api.auth.dto.UserSignInDto;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.domain.auth.service.AuthService;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenValidator;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.util.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final StudentService studentService;
    private final EmailService emailService;
    private final TokenValidator tokenValidator;

    // 인증 테스트
    @GetMapping("/")
    public ResTemplate<String> test() {
        return new ResTemplate<>(HttpStatus.OK, "인증 테스트");
    }

    // 로그인
    @PostMapping("/auth/sign-in")
    public ResTemplate<TokenDto> handleSignIn(@RequestBody @Valid UserSignInDto.Request singInRequest){
        TokenDto tokenDto = authService.signInTemp(singInRequest.getEmail(), singInRequest.getPassword());
        return new ResTemplate<>(HttpStatus.OK, tokenDto);
    }

    // 로그인V2
    @PostMapping("/auth/sign-in/short-token-exp")
    public ResTemplate<TokenDto> handleSignInV2(@RequestBody @Valid UserSignInDto.Request singInRequest){
        TokenDto tokenDto = authService.signIn(singInRequest.getEmail(), singInRequest.getPassword());
        return new ResTemplate<>(HttpStatus.OK, tokenDto);
    }

    /**
     *   refresh 토큰을 이용, access 토큰을 재발급하는 메소드
     */
    @PostMapping(value = "/auth/token/reissue")
    public ResTemplate<TokenDto> accessToken(HttpServletRequest httpServletRequest){

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        tokenValidator.validateBearer(authorizationHeader);

        String refreshToken = authorizationHeader.split(" ")[1];
        TokenDto tokenDto = authService.reassureByRefreshToken(refreshToken);

        return new ResTemplate<>(HttpStatus.OK, "토큰 재발급", tokenDto);
    }


    /** 메일 인증
     * 1. 이메일 인증코드를 만들어서 이메일로 보내기
     * 2. 이메일과 인증코드를 한 쌍으로 20분간 기억하기 (이후 회원가입 API에서 인증코드가 만료되었는지 체크)
     */
    @PostMapping("/auth/email")
    public ResTemplate<String> handleEmailVerification(@RequestBody @Valid EmailPrefix emailPrefix) {
//        String email = emailPrefix.getEmailPrefix() + "@office.skhu.ac.kr"; // skhu.ac.kr 이메일만 허용한다.
        String email = emailPrefix.getEmailPrefix() + "@gmail.com"; // 학교메일로 테스트하기 킹받아서 gmail로 바꿈

        studentService.checkEmailDuplicated(email); // 이메일이 이미 존재하는지 체크한다.
        emailService.sendEmailVerificationCode(email); // 이메일로 인증코드를 보낸다.

        return new ResTemplate<>(HttpStatus.OK,
                email + "로 검증코드를 전송했습니다.");
    }
    @NoArgsConstructor
    @Getter
    static class EmailPrefix{ @NotBlank private String emailPrefix;}
}