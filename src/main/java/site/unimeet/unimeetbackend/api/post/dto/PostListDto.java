package site.unimeet.unimeetbackend.api.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import site.unimeet.unimeetbackend.api.common.SliceInfoDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.enums.State;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;

import java.util.List;
import java.util.stream.Collectors;

// 게시글 목록
public class PostListDto {
    @Getter
    public static class Res{
        List<PostDto> posts;
        SliceInfoDto page;
        public static PostListDto.Res from(Slice<Post> postList){
            return new PostListDto.Res(PostDto.from(postList), SliceInfoDto.from(postList));
        }

        public Res(List<PostDto> posts, SliceInfoDto page) {
            this.posts = posts;
            this.page = page;
        }

        @Getter
        @Builder
        private static class PostDto{
            long id;
            String title;
            String content;
            String imageUrl;
            State state;
            int maxPeople;
            Gender gender;
            int likes;

            static PostListDto.Res.PostDto from(Post post){
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
            static List<PostListDto.Res.PostDto> from (Slice<Post> postList){
                return postList.stream()
                        .map(PostListDto.Res.PostDto::from)
                        .collect(Collectors.toList());
            }
        }
    }


}
