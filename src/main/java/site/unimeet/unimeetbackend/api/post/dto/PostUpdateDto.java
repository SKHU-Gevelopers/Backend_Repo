package site.unimeet.unimeetbackend.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
public class PostUpdateDto {


    private String title;

    private String content;
//    @NotNull(message = "이미지가 NULL입니다.")
//    private List<MultipartFile> postImages;

    private int maxPeople;

    private Gender gender;

    public Post toEntity(List<String> imageUrls){
        return Post.builder()
                .title(title)
                .content(content)
                .maxPeople(maxPeople)
                .gender(gender)
                .build();
    }
}
