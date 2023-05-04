package site.unimeet.unimeetbackend.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.Department;
import site.unimeet.unimeetbackend.domain.common.Major;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Major major;
    @Enumerated(EnumType.STRING)
    private Department department;

    @Builder
    public User(String name, String email, String password, Major major, Department department){
        this.name=name;
        this.email=email;
        this.password = password;
        this.major = major;
        this.department=department;
    }
}
