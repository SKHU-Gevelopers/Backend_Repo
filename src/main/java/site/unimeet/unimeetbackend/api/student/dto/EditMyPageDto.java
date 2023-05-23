package site.unimeet.unimeetbackend.api.student.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import site.unimeet.unimeetbackend.domain.student.enums.Major;
import site.unimeet.unimeetbackend.domain.student.enums.Mbti;
import site.unimeet.unimeetbackend.domain.student.Student;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EditMyPageDto {
    @Getter
    @AllArgsConstructor
    public static class Request {
        @Length(min = 2, max = 10, message = "이름은 2~10자 사이어야 합니다")
        private String nickname;
        @NotNull
        private Mbti mbti;
        private String introduction;
        @NotNull
        private List<Major> majors;
        private MultipartFile profileImg;

        public void editMyPage(Student student, String profileImageUrl) {
            student.editMyPage(nickname, mbti, introduction, profileImageUrl, majors);
        }
    }
}
