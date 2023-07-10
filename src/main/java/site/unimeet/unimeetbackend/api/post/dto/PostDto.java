package site.unimeet.unimeetbackend.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@AllArgsConstructor
@Data
public class PostDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private List<MultipartFile> postImages;
    @Min(1)
    private int maxPeople;
    @NotNull
    private Gender gender;

    public Post toEntity(List<String> imageUrls){
        return Post.builder()
                .title(title)
                .content(content)
                .imageUrls(imageUrls)
                .maxPeople(maxPeople)
                .gender(gender)
                .build();
    }

}
