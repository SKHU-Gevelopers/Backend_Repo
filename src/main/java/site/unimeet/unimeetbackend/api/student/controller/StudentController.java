package site.unimeet.unimeetbackend.api.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.api.student.dto.EditMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.MyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.PublicMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.UserSignUpDto;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;
import site.unimeet.unimeetbackend.util.PageableUtil;
import site.unimeet.unimeetbackend.util.S3Service;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class StudentController {
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    // todo 인스턴스 변수명과 빈 이름이 어떤 관계인지 파악
    private final S3Service s3Service;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ResTemplate<String>> handleSignUp(@RequestBody @Valid UserSignUpDto.Request signUpRequest){
        Student student = studentService.signUp(signUpRequest.toEntity(passwordEncoder));

        ResTemplate<String> resTemplate = new ResTemplate<>(HttpStatus.CREATED,
                student.getNickname() + " is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
    }

    // 마이페이지 수정
    @PostMapping("/my-page")
    public ResponseEntity<Void> handleEditMyPage(@ModelAttribute @Valid EditMyPageDto.Request editMyPageRequest
                                , @StudentEmail String email) {
        String uploadedFileUrl = s3Service.upload(editMyPageRequest.getProfileImg(), S3Config.BUCKETNAME_SUFFIX_PROFILE_IMG);
        studentService.editMyPage(editMyPageRequest, uploadedFileUrl, email);

        return ResponseEntity.noContent().build();
    }

    // 마이페이지 수정창 조회
    @GetMapping("/my-page")
    public ResTemplate<MyPageDto.Rsp> handleGetPrivateMyPage(@StudentEmail String email) {
        MyPageDto.Rsp rspDto = studentService.getMyPage(email);

        return new ResTemplate<>(HttpStatus.OK, rspDto);
    }

     // 공개 마이페이지 조회
    @GetMapping("/{userId}/my-page")
    public ResTemplate<PublicMyPageDto.Res> handleGetMyPage(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "1") final int page) {
        final int GUESTBOOK_PAGE_SIZE = 6;
        Pageable pageable = PageableUtil.of(page, GUESTBOOK_PAGE_SIZE);

        PublicMyPageDto.Res rspsDto = studentService.getPublicMyPage(userId, pageable);

        return new ResTemplate<>(HttpStatus.OK, rspsDto);
    }
}













