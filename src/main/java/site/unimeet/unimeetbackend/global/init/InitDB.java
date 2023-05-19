package site.unimeet.unimeetbackend.global.init;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.common.Department;
import site.unimeet.unimeetbackend.domain.common.Gender;
import site.unimeet.unimeetbackend.domain.common.Major;
import site.unimeet.unimeetbackend.domain.common.Mbti;
import site.unimeet.unimeetbackend.domain.user.Student;
import site.unimeet.unimeetbackend.domain.user.StudentRepository;


import javax.annotation.PostConstruct;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${default.profile.image.url}")
    private String defaultProfileImageUrl;

    @PostConstruct
    public void init() {
        ArrayList<Major> majors = new ArrayList<>();
        majors.add(Major.AI);
        Student student = new Student("김성김", "nickname", "eeee@email.com", passwordEncoder.encode("pppp"),
                Gender.MALE, Mbti.ENFJ, defaultProfileImageUrl, majors, Department.IT);
        studentRepository.save(student);
    }
}
