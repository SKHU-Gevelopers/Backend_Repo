package site.unimeet.unimeetbackend.api.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.SingleRspsTemplate;
import site.unimeet.unimeetbackend.api.student.dto.EditMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.GetMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.UserSignUpDto;
import site.unimeet.unimeetbackend.api.student.dto.WriteGuestBookDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;
import site.unimeet.unimeetbackend.util.S3Service;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class StudentController {
    private final StudentService studentService;
    private final GuestBookService guestBookService;
    private final PasswordEncoder passwordEncoder;
    // todo 인스턴스 변수명과 빈 이름이 어떤 관계인지 파악
    private final S3Service s3Service;

    // 회원가입
    @PostMapping("/users/sign-up")
    public ResponseEntity<SingleRspsTemplate<String>> signUp(@RequestBody @Valid UserSignUpDto.Request signUpRequest){
        Student student = studentService.signUp(signUpRequest.toEntity(passwordEncoder), signUpRequest.getEmailVrfCode());

        SingleRspsTemplate<String> rspsTemplate = new SingleRspsTemplate<>(HttpStatus.CREATED.value(),
                student.getNickname() + " is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }

    // 마이페이지 수정
    @PostMapping("/users/my-page")
    public ResponseEntity<?> editMyPage(@ModelAttribute EditMyPageDto.Request editMyPageRequest
                                , @StudentEmail String email) {
        String uploadedFilePath = s3Service.upload(editMyPageRequest.getProfileImg(), S3Config.BUCKETNAME_SUFFIX_PROFILE_IMG);
        studentService.editMyPage(editMyPageRequest, uploadedFilePath, email);

        return ResponseEntity.noContent().build();
    }

    // 마이페이지 조회
    @GetMapping("/users/my-page")
    public SingleRspsTemplate<GetMyPageDto.Response> editMyPage(@StudentEmail String email) {
        GetMyPageDto.Response rspsDto = studentService.getMyPage(email);

        SingleRspsTemplate<GetMyPageDto.Response> rspsTemplate = new SingleRspsTemplate<>(HttpStatus.OK.value(), rspsDto);
        return rspsTemplate;
    }

    // 방명록 작성
    @PostMapping("/users/{userId}/guestbooks")
    public ResponseEntity<?> writeGuestbook(@StudentEmail String writerEmail,
                                            @PathVariable("userId") Long targetUserId, @RequestBody WriteGuestBookDto.Req reqDto) {
        guestBookService.write(targetUserId, reqDto.getContent(), writerEmail);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}













