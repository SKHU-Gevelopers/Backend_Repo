package site.unimeet.unimeetbackend.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.unimeet.unimeetbackend.api.auth.dto.UserSignInDto;
import site.unimeet.unimeetbackend.api.common.SingleRspsTemplate;
import site.unimeet.unimeetbackend.domain.auth.AuthService;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    // 로그인
    @PostMapping("/users/sign-in")
    public SingleRspsTemplate<TokenDto> signIn(@RequestBody @Valid UserSignInDto.Request singInRequest){
        TokenDto tokenDto = authService.signIn(singInRequest.getEmail(), singInRequest.getPassword());
        return new SingleRspsTemplate<>(HttpStatus.OK.value(), tokenDto);
    }

}