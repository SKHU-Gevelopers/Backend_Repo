package site.unimeet.unimeetbackend.api.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //회원가입
    @PostMapping("/users/sign-up")
    public ResponseEntity<SingleRspsTemplate<String>> signUp(@RequestBody @Valid UserSignUpDto.Request signUpRequest){
        User user = userService.signUp(signUpRequest.toEntity(passwordEncoder));
        SingleRspsTemplate<String> rspsTemplate = new SingleRspsTemplate<>(HttpStatus.CREATED.value(),
                user.getName() + " is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);

    }
}
