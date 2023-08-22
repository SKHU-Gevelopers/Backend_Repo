package site.unimeet.unimeetbackend.api.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReadDto {

    @NotNull(message = "게시글 번호를 입력해주세요")
    private Long postId;
}
