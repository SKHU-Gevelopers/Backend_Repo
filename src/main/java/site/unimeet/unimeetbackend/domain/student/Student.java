package site.unimeet.unimeetbackend.domain.student;

import lombok.*;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.common.Department;
import site.unimeet.unimeetbackend.domain.common.Gender;
import site.unimeet.unimeetbackend.domain.common.Major;
import site.unimeet.unimeetbackend.domain.common.Mbti;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
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

    @Builder
    public Student(String name, String nickname, String email, String password, Gender gender, Mbti mbti, String profileImageUrl,List<Major> majors, Department department) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mbti = mbti;
        this.profileImageUrl = profileImageUrl;
        this.majors = majors;
        this.department = department;
    }
}
