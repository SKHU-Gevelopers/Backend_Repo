package site.unimeet.unimeetbackend.api.student.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.student.Student;

// 사용자 식별자와 이름을 포함함.
@Getter
@Builder
public class StudentIdAndNickNameDto {
    private Long id;
    private String nickname;

    public static StudentIdAndNickNameDto from(Student student) {
        return StudentIdAndNickNameDto.builder()
                .id(student.getId())
                .nickname(student.getNickname())
                .build();
    }
}
