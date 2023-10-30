package site.unimeet.unimeetbackend.domain.student;

import lombok.*;
import site.unimeet.unimeetbackend.domain.student.component.enums.Department;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;
import site.unimeet.unimeetbackend.domain.student.component.enums.Mbti;
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
    @Column(unique = true, nullable = false)
    private String nickname;

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

    @Column(unique = true)
    private String refreshToken;
    @Column(nullable = false, unique = true)
    private String kakaoIdTokenSub;
    private LocalDateTime refreshTokenExp;

    @Builder
    private Student(String nickname, String email, String password, Gender gender
            , Mbti mbti, String profileImageUrl,List<Major> majors, Department department, String kakaoIdTokenSub) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mbti = mbti;
        this.profileImageUrl = profileImageUrl;
        this.majors = majors;
        this.department = department;
        this.kakaoIdTokenSub = kakaoIdTokenSub;
    }

    public void editMyPage(String nickname, Mbti mbti, String introduction, String profileImageUrl, List<Major> majors){
        this.nickname = nickname;
        this.mbti = mbti;
        this.introduction = introduction;
        this.majors = majors;
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    public void updateRefreshTokenAndExp(String refreshToken, Date refreshTokenExp) {
        this.refreshToken = refreshToken;
        this.refreshTokenExp = DateTimeUtils.convertToLocalDateTime(refreshTokenExp);
    }

    // 로그아웃 혹은 리프레시 토큰 정보를 무효화할 때 사용.
    public void logout(){
        this.refreshToken = "";
        this.refreshTokenExp = LocalDateTime.now();
    }
}
