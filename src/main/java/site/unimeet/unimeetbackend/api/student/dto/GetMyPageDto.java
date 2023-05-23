package site.unimeet.unimeetbackend.api.student.dto;


import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.common.Gender;
import site.unimeet.unimeetbackend.domain.common.Major;
import site.unimeet.unimeetbackend.domain.common.Mbti;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;

public class GetMyPageDto {
    @Getter
    @Builder
    public static class Response{
        private String nickname;
        private short age;
        private Gender gender;
        private Mbti mbti;
        private String introduction;
        private String profileImageUrl;
        private List<Major> majors;

        public static Response of(Student student) {
            return Response.builder()
                    .nickname(student.getNickname())
                    .age(student.getAge())
                    .gender(student.getGender())
                    .mbti(student.getMbti())
                    .introduction(student.getIntroduction())
                    .profileImageUrl(student.getProfileImageUrl())
                    .majors(student.getMajors())
                    .build();
        }
    }
}
