package site.unimeet.unimeetbackend.api.post.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.enums.State;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;

import java.util.List;
import java.util.stream.Collectors;

// 게시글 목록
public class PostListDto {
    @Getter
    @Builder
    public static class Res{
        private List<PostDto> posts;
        public static PostListDto.Res from(List<Post> postList){
            List<PostDto> postDtos = PostDto.from(postList);
            return PostListDto.Res.builder()
                    .posts(postDtos)
                    .build();
        }

        @Getter
        @Builder
        private static class PostDto{
            private Long id;
            private String title;
            private String content;
            private String imageUrl;
            private State state;
            private int maxPeople;
            private Gender gender;
            private int likes;

            private static PostListDto.Res.PostDto from(Post post){
                List<String> imageUrls = post.getImageUrls();
                String imageUrl = imageUrls.size() > 0 ? imageUrls.get(0) : "";

                return PostListDto.Res.PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imageUrl(imageUrl)
                        .state(post.getState())
                        .maxPeople(post.getMaxPeople())
                        .gender(post.getGender())
                        .likes(post.getLikes())
                        .build();
            }
            private static List<PostListDto.Res.PostDto> from (List<Post> postList){
                return postList.stream()
                        .map(PostListDto.Res.PostDto::from)
                        .collect(Collectors.toList());
            }
        }
    }


}
