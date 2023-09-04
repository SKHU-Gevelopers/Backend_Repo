package site.unimeet.unimeetbackend.domain.student;

import lombok.*;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthenticationException;
import site.unimeet.unimeetbackend.util.DateTimeUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String kakaoId;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Column(nullable = false)
    private String introduction = "";
    @Setter
    @Column(nullable = false)
    private String profileImageUrl  = "";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Department department;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Major> majors;

    private String refreshToken;

    private LocalDateTime tokenExp;


    @Builder
    private Student(String name, String nickname, String email, String password, Gender gender, Mbti mbti, String kakaoId, String profileImageUrl,List<Major> majors, Department department) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mbti = mbti;
        this.kakaoId = kakaoId;
        this.profileImageUrl = profileImageUrl;
        this.majors = majors;
        this.department = department;
    }

    public void editMyPage(String nickname, Mbti mbti, String introduction, String profileImageUrl, String kakaoId,List<Major> majors){
        this.nickname = nickname;
        this.mbti = mbti;
        this.introduction = introduction;
        this.majors = majors;
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
        this.kakaoId = kakaoId;
    }

    public void updateRefreshTokenAndExp(String refreshToken, Date refreshTokenExp) {
        this.refreshToken = refreshToken;
        this.tokenExp = DateTimeUtils.convertToLocalDateTime(refreshTokenExp);
    }

    public void logout(){
        this.refreshToken = "";
        this.tokenExp = LocalDateTime.now();
    }

    public void validateRefreshTokenExp() {
        if(tokenExp.isBefore(LocalDateTime.now())){
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }
}
