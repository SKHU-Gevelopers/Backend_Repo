package site.unimeet.unimeetbackend.api.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.unimeet.unimeetbackend.domain.common.Department;
import site.unimeet.unimeetbackend.domain.common.Gender;
import site.unimeet.unimeetbackend.domain.common.Major;
import site.unimeet.unimeetbackend.domain.common.Mbti;
import site.unimeet.unimeetbackend.domain.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

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
        @NotNull
        private Gender gender;
        @NotNull
        private Mbti mbti;
        @NotNull
        private Department department;
        @NotNull
        private List<Major> majors;

        public User toEntity(PasswordEncoder passwordEncoder){
            String encodedPassword = passwordEncoder.encode(this.password);
            return User.builder()
                    .name(name)
                    .nickname(nickname)
                    .email(email)
                    .password(encodedPassword)
                    .gender(gender)
                    .mbti(mbti)
                    .majors(majors)
                    .department(department)
                    .build();
        }
    }


}