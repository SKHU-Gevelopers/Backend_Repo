package site.unimeet.unimeetbackend.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.Department;
import site.unimeet.unimeetbackend.domain.common.Gender;
import site.unimeet.unimeetbackend.domain.common.Major;
import site.unimeet.unimeetbackend.domain.common.Mbti;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private Mbti mbti;
    @Column
    private String introduction = "";
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> profileImageUrls;
    @Enumerated(EnumType.STRING)
    private Department department;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Major> majors;

    @Builder
    public User(String name, String nickname, String email, String password, Gender gender, Mbti mbti, List<Major> majors, Department department) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mbti = mbti;
        this.majors = majors;
        this.department = department;
    }
}
