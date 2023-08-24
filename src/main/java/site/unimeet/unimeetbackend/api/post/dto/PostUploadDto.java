package site.unimeet.unimeetbackend.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

// 게시글 업로드
@AllArgsConstructor
@Data
public class PostUploadDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    @NotNull(message = "이미지가 NULL입니다.")
    private List<MultipartFile> postImages;
    @Min(1)
    private int maxPeople;
    @NotNull
    private Gender gender;

    public Post toEntity(List<String> imageUrls, Student writer){
        return Post.builder()
                .title(title)
                .content(content)
                .imageUrls(imageUrls)
                .maxPeople(maxPeople)
                .gender(gender)
                .writer(writer)
                .build();
    }

}
