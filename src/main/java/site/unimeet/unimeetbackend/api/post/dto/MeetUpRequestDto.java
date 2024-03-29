package site.unimeet.unimeetbackend.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.domain.meetup.MeetUp;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MeetUpRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Req{
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;
        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
        @NotBlank(message = "연락처를 입력해주세요.")
        private String contact;
        @NotNull(message = "이미지 전송 오류입니다.")
        private List<MultipartFile> meetUpImage;

        public MeetUp toEntity(List<String> meetUpImageUrls, Post targetPost, Student sender, Student receiver){
            return MeetUp.builder()
                    .title(title)
                    .content(content)
                    .contact(contact)
                    .imageUrls(meetUpImageUrls)
                    .targetPost(targetPost)
                    .sender(sender)
                    .receiver(receiver)
                    .build();
        }
    }
}
