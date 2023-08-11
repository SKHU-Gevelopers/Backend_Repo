package site.unimeet.unimeetbackend.api.student.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;

import javax.validation.constraints.*;
import java.util.List;

// 회원가입
public class UserSignUpDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @Length(min = 2, max = 10, message = "이름은 2~10자 사이여야 합니다")
        private String name;
        @Length(min = 2, max = 10, message = "이름은 2~10자 사이여야 합니다")
        private String nickname;
        @Email(message = "Email 형식이어야 합니다")
        private String email;
        @Length(min = 4, max = 20, message = "비밀번호는 4~20자 사이여야 합니다")
        private String password;
        @NotBlank(message = "이메일 인증 코드는 비어있을 수 없습니다")
        private String emailVrfCode;
        @NotNull
        private Gender gender;
        @NotNull
        private Mbti mbti;
        @NotNull
        @Length(min = 1, max = 20, message = "카카오 아이디는 1~20자 사이여야 합니다")
        private String kakaoId;
        @NotNull
        private Department department;
        @NotNull
        private List<Major> majors;
        private final String defaultProfileImageUrl = "https://unimeet-bucket.s3.ap-northeast-2.amazonaws.com/user_profile_img/ae5e08b9-9b37-433f-a2a5-03d720bd853e.png";
        public Student toEntity(PasswordEncoder passwordEncoder){
            String encodedPassword = passwordEncoder.encode(this.password);
            return Student.builder()
                    .name(name)
                    .nickname(nickname)
                    .email(email)
                    .password(encodedPassword)
                    .gender(gender)
                    .mbti(mbti)
                    .kakaoId(kakaoId)
                    .profileImageUrl(defaultProfileImageUrl)
                    .majors(majors)
                    .department(department)
                    .build();
        }
    }


}