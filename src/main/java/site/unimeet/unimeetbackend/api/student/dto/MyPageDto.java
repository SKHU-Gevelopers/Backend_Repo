package site.unimeet.unimeetbackend.api.student.dto;


import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;

// 마이페이지 조회
public class MyPageDto {
    @Getter
    @Builder
    public static class Rsp {
        String nickname;
        Gender gender;
        Mbti mbti;
        String introduction;
        String profileImageUrl;
        String kakaoId;
        private List<Major> majors;

        public static Rsp of(Student student) {
            return Rsp.builder()
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
