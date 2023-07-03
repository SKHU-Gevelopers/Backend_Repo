package site.unimeet.unimeetbackend.api.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.enums.State;
import site.unimeet.unimeetbackend.domain.student.enums.Gender;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
    private State state;
    private int maxPeople;
    private Gender gender;
    private int likes;

    public static PostListDto of(Post post){
        Hibernate.initialize(post.getTitle());
        return PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrls(post.getImageUrls())
                .state(post.getState())
                .maxPeople(post.getMaxPeople())
                .gender(post.getGender())
                .likes(post.getLikes())
                .build();
    }
    public static List<PostListDto> of (List<Post> postList){
        return postList.stream()
                .map(PostListDto::of)
                .collect(Collectors.toList());
    }

    @Builder
    public PostListDto(Long id,String title, String content, List<String> imageUrls, State state, int maxPeople, Gender gender, int likes){
        this.id =id;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.state = state;
        this.maxPeople = maxPeople;
        this.gender = gender;
        this.likes = likes;
    }
}
