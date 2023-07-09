package site.unimeet.unimeetbackend.api.student.dto;


import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;

public class GetMyPageDto {
    @Getter
    @Builder
    public static class Response{
        private String nickname;
        private Gender gender;
        private Mbti mbti;
        private String introduction;
        private String profileImageUrl;
        private List<Major> majors;

        public static Response of(Student student) {
            return Response.builder()
                    .nickname(student.getNickname())
                    .gender(student.getGender())
                    .mbti(student.getMbti())
                    .introduction(student.getIntroduction())
                    .profileImageUrl(student.getProfileImageUrl())
                    .majors(student.getMajors())
                    .build();
        }
    }
}
