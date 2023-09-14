package site.unimeet.unimeetbackend.api.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.comment.Comment;
import site.unimeet.unimeetbackend.domain.student.Student;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {

    private Long id;
    private String content;
    private StudentIdAndNickNameDto student;

    public static CommentDto from(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                StudentIdAndNickNameDto.from(comment.getStudent())
        );

    }
}
