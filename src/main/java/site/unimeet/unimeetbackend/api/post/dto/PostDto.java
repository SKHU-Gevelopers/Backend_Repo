package site.unimeet.unimeetbackend.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.enums.State;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
    private State state;
    private int maxPeople;
    private Gender gender;
    private int likes;

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .imageUrls(imageUrls)
                .state(state)
                .maxPeople(maxPeople)
                .gender(gender)
                .build();
    }

}
