package site.unimeet.unimeetbackend.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.unimeet.unimeetbackend.api.auth.dto.UserSignInDto;
import site.unimeet.unimeetbackend.api.common.SingleRspsTemplate;
import site.unimeet.unimeetbackend.domain.auth.service.AuthService;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.user.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    // 로그인
    // todo 회원가입 DTO 손보고, 인증번호 만료되었는지 체크
    @PostMapping("/auth/sign-in")
    public SingleRspsTemplate<TokenDto> signIn(@RequestBody @Valid UserSignInDto.Request singInRequest){
        TokenDto tokenDto = authService.signIn(singInRequest.getEmail(), singInRequest.getPassword());
        return new SingleRspsTemplate<>(HttpStatus.OK.value(), tokenDto);
    }

    /** 메일 인증
     * 1. 이메일 인증코드를 만들어서 이메일로 보내기
     * 2. 이메일과 인증코드를 한 쌍으로 20분간 기억하기 (이후 회원가입 API에서 인증코드가 만료되었는지 체크)
     */
    @PostMapping("/auth/email")
    public SingleRspsTemplate<String> test(@RequestBody String emailPrefix) {
        String email = emailPrefix + "@skhu.ac.kr"; // skhu.ac.kr 이메일만 허용한다.

        userService.checkEmailDuplicated(email); // 이메일이 이미 존재하는지 체크한다.
        authService.sendEmailVerificationCode(email); // 이메일로 인증코드를 보낸다.

        return new SingleRspsTemplate<>(HttpStatus.OK.value(),
                email + "로 인증코드를 전송했습니다.");
    }



}