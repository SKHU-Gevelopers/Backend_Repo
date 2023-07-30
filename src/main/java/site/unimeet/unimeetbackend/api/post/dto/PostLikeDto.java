package site.unimeet.unimeetbackend.api.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeDto {

    private Long studentId;
    private Long postId;

    public PostLikeDto(Long studentId, Long postId){
        this.studentId = studentId;
        this.postId = postId;
    }
}
