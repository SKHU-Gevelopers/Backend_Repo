package site.unimeet.unimeetbackend.api.student.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.student.Student;

@Getter
@Builder
public class StudentIdAndNameDto {
    private Long id;
    private String name;

    public static StudentIdAndNameDto from(Student student) {
        return StudentIdAndNameDto.builder()
                .id(student.getId())
                .name(student.getName())
                .build();
    }
}
