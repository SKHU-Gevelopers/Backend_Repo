package site.unimeet.unimeetbackend.api.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.api.common.SingleRspsTemplate;
import site.unimeet.unimeetbackend.api.student.dto.UserSignUpDto;
import site.unimeet.unimeetbackend.domain.user.Student;
import site.unimeet.unimeetbackend.domain.user.StudentService;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class StudentController {
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    // todo 인스턴스 변수명과 빈 이름이 어떤 관계인지 파악

    @PostMapping("/users/sign-up")
    public ResponseEntity<SingleRspsTemplate<String>> signUp(@RequestBody @Valid UserSignUpDto.Request signUpRequest){
        Student student = studentService.signUp(signUpRequest.toEntity(passwordEncoder), signUpRequest.getEmailVrfCode());

        SingleRspsTemplate<String> rspsTemplate = new SingleRspsTemplate<>(HttpStatus.CREATED.value(),
                student.getNickname() + " is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }


    // 마이페이지 수정
    @GetMapping("/test")
    public String hello(MultipartFile multipartFile, @StudentEmail String email) {
        studentService.uploadProfileImg(multipartFile, email);

        return "hello";
    }



}













