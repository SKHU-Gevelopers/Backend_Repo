package site.unimeet.unimeetbackend.api.student.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.RspsTemplate;
import site.unimeet.unimeetbackend.api.student.dto.*;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;
import site.unimeet.unimeetbackend.util.S3Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


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
    public ResponseEntity<RspsTemplate<String>> handleSignUp(@RequestBody @Valid UserSignUpDto.Request signUpRequest){
        Student student = studentService.signUp(signUpRequest.toEntity(passwordEncoder), signUpRequest.getEmailVrfCode());

        RspsTemplate<String> rspsTemplate = new RspsTemplate<>(HttpStatus.CREATED,
                student.getNickname() + " is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspsTemplate);
    }

    // 마이페이지 수정
    @PostMapping("/users/my-page")
    public ResponseEntity<?> handleEditMyPage(@ModelAttribute @Valid EditMyPageDto.Request editMyPageRequest
                                , @StudentEmail String email) {
        String uploadedFileUrl = s3Service.upload(editMyPageRequest.getProfileImg(), S3Config.BUCKETNAME_SUFFIX_PROFILE_IMG);
        studentService.editMyPage(editMyPageRequest, uploadedFileUrl, email);

        return ResponseEntity.noContent().build();
    }

    // 마이페이지 수정창 조회
    @GetMapping("/users/my-page")
    public RspsTemplate<MyPageDto.Response> handleGetPrivateMyPage(@StudentEmail String email) {
        MyPageDto.Response rspsDto = studentService.getMyPage(email);

        RspsTemplate<MyPageDto.Response> rspsTemplate = new RspsTemplate<>(HttpStatus.OK, rspsDto);
        return rspsTemplate;
    }

    // 방명록 작성
    @PostMapping("/users/{userId}/guestbooks")
    public ResponseEntity<?> handleWriteGuestbook(@StudentEmail String writerEmail,
                                            @PathVariable("userId") Long targetUserId, @RequestBody @Valid WriteGuestBookDto reqDto) {
        guestBookService.write(targetUserId, reqDto.getContent(), writerEmail);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DTO 크기가 매우 작아 따로 관리할 필요가 없을 것으로 판단 Inner Class 사용
    @Getter
    @NoArgsConstructor
    private static class WriteGuestBookDto {
        @NotBlank
        private String content;
    }

     // 공개 마이페이지 조회
    @GetMapping("/users/{userId}/my-page")
    public RspsTemplate<PublicMyPageDto.Res> handleGetMyPage(@PathVariable Long userId) {
        PublicMyPageDto.Res rspsDto = studentService.getPublicMyPage(userId);

        RspsTemplate<PublicMyPageDto.Res> rspsTemplate = new RspsTemplate<>(HttpStatus.OK, rspsDto);
        return rspsTemplate;
    }
}













