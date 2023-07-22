package site.unimeet.unimeetbackend.api.student.component.guestbook.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.unimeet.unimeetbackend.domain.student.component.guestbook.GuestBookService;
import site.unimeet.unimeetbackend.global.resolver.StudentEmail;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class GuestBookController {
    private final GuestBookService guestBookService;

    // 방명록 작성
    @PostMapping("/users/{userId}/guestbooks")
    public ResponseEntity<?> handleWriteGuestbook(@StudentEmail String writerEmail,
                                                  @PathVariable("userId") Long targetUserId, @RequestBody @Valid WriteGuestBookDto reqDto) {
        guestBookService.write(targetUserId, reqDto.getContent(), writerEmail);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // DTO 크기가 매우 작아 따로 관리할 필요가 없을 것으로 판단. Inner Class 사용
    @Getter
    @NoArgsConstructor
    private static class WriteGuestBookDto {
        @NotBlank(message = "방명록 내용을 입력해주세요.")
        private String content;
    }
}
