package site.unimeet.unimeetbackend.api.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.api.student.dto.EditMyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.MyPageDto;
import site.unimeet.unimeetbackend.api.student.dto.PublicMyPageDto;
import site.unimeet.unimeetbackend.domain.student.StudentService;
import site.unimeet.unimeetbackend.global.config.cloud.S3Config;
import site.unimeet.unimeetbackend.global.resolver.StudentId;
import site.unimeet.unimeetbackend.util.PageableUtil;
import site.unimeet.unimeetbackend.util.S3Service;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class StudentController {
    private final StudentService studentService;
    // todo 인스턴스 변수명과 빈 이름이 어떤 관계인지 파악
    private final S3Service s3Service;

    // 마이페이지 수정
    @PostMapping("/my-page")
    public ResponseEntity<Void> handleEditMyPage(@ModelAttribute @Valid EditMyPageDto.Request editMyPageRequest
                                , @StudentId long loggedInId) {
        String uploadedFileUrl = s3Service.upload(editMyPageRequest.getProfileImg(), S3Config.BUCKETNAME_SUFFIX_PROFILE_IMG);
        studentService.editMyPage(editMyPageRequest, uploadedFileUrl, loggedInId);

        return ResponseEntity.noContent().build();
    }

    // 마이페이지 수정창 조회
    @GetMapping("/my-page")
    public ResTemplate<MyPageDto.Rsp> handleGetPrivateMyPage(@StudentId long loggedInId) {
        MyPageDto.Rsp rspDto = studentService.getMyPage(loggedInId);

        return new ResTemplate<>(HttpStatus.OK, rspDto);
    }

     // 공개 마이페이지 조회
    @GetMapping("/{userId}/my-page")
    public ResTemplate<PublicMyPageDto.Res> handleGetMyPage(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "1") final int page) {
        final int GUESTBOOK_PAGE_SIZE = 6;
        Pageable pageable = PageableUtil.of(page, GUESTBOOK_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));

        PublicMyPageDto.Res rspsDto = studentService.getPublicMyPage(userId, pageable);

        return new ResTemplate<>(HttpStatus.OK, rspsDto);
    }

    // 나의 공개 마이페이지 조회.
    @GetMapping("/my-page-pub")
    public ResTemplate<PublicMyPageDto.Res> handleGetPublicMyPage(@StudentId long loggedInId,
                                                            @RequestParam(defaultValue = "1") final int page) {
        final int GUESTBOOK_PAGE_SIZE = 6;
        Pageable pageable = PageableUtil.of(page, GUESTBOOK_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));

        PublicMyPageDto.Res rspsDto = studentService.getPublicMyPage(loggedInId, pageable);

        return new ResTemplate<>(HttpStatus.OK, rspsDto);
    }
}













