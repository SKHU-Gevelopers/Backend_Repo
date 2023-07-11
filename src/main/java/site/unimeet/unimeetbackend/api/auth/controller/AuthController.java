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
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.domain.auth.service.AuthService;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.student.StudentService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final StudentService studentService;

    // 인증 테스트
    @GetMapping("/")
    public RspsTemplate<String> test() {
        return new RspsTemplate<>(HttpStatus.OK, "인증 테스트");
    }

    // 로그인
    @PostMapping("/auth/sign-in")
    public RspsTemplate<TokenDto> handleSignIn(@RequestBody @Valid UserSignInDto.Request singInRequest){
        TokenDto tokenDto = authService.signIn(singInRequest.getEmail(), singInRequest.getPassword());
        return new RspsTemplate<>(HttpStatus.OK, tokenDto);
    }

    /** 메일 인증
     * 1. 이메일 인증코드를 만들어서 이메일로 보내기
     * 2. 이메일과 인증코드를 한 쌍으로 20분간 기억하기 (이후 회원가입 API에서 인증코드가 만료되었는지 체크)
     */
    @PostMapping("/auth/email")
    public RspsTemplate<String> handleEmailVerification(@RequestBody @Valid EmailPrefix emailPrefix) {
//        String email = emailPrefix.getEmailPrefix() + "@office.skhu.ac.kr"; // skhu.ac.kr 이메일만 허용한다.
        String email = emailPrefix.getEmailPrefix() + "@gmail.com"; // 학교메일로 테스트하기 킹받아서 gmail로 바꿈

        studentService.checkEmailDuplicated(email); // 이메일이 이미 존재하는지 체크한다.
        authService.sendEmailVerificationCode(email); // 이메일로 인증코드를 보낸다.

        return new RspsTemplate<>(HttpStatus.OK,
                email + "로 검증코드를 전송했습니다.");
    }
    @NoArgsConstructor
    @Getter
    static class EmailPrefix{ @NotBlank private String emailPrefix;}
}