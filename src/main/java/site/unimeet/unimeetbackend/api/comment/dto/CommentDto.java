package site.unimeet.unimeetbackend.api.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.api.student.dto.StudentProfileDto;
import site.unimeet.unimeetbackend.domain.comment.Comment;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {

    private Long id;
    private String content;
    private StudentProfileDto student;

    public static CommentDto from(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                StudentProfileDto.from(comment.getStudent())
        );

    }
}
