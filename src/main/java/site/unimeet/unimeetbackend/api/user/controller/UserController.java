package site.unimeet.unimeetbackend.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.unimeet.unimeetbackend.api.common.SingleRspsTemplate;
import site.unimeet.unimeetbackend.api.user.dto.UserSignUpDto;
import site.unimeet.unimeetbackend.domain.user.User;
import site.unimeet.unimeetbackend.domain.user.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    // todo 회원가입.
    //  이메일 인증 API를 따로 둘 것.
    //   회원가입 시 이메일 인증코드가 만료되었는지 체크하면 된다.
    @PostMapping("/users/sign-up")
    public ResponseEntity<SingleRspsTemplate<String>> signUp(@RequestBody @Valid UserSignUpDto.Request signUpRequest){
        User user = userService.signUp(signUpRequest.toEntity(passwordEncoder));
        SingleRspsTemplate<String> rspsTemplate = new SingleRspsTemplate<>(HttpStatus.CREATED.value(),
                user.getNickname() + " is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }


}
