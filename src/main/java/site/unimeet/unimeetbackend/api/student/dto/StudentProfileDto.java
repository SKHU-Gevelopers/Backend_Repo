package site.unimeet.unimeetbackend.api.student.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.student.Student;

// 사용자 식별자, 이름, 프로필 사진 url을 포함.
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class StudentProfileDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public static StudentProfileDto from(Student student) {
        return StudentProfileDto.builder()
                .id(student.getId())
                .nickname(student.getNickname())
                .profileImageUrl(student.getProfileImageUrl())
                .build();
    }
}
