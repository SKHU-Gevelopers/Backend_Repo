package site.unimeet.unimeetbackend.global.init;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.common.Department;
import site.unimeet.unimeetbackend.domain.common.Gender;
import site.unimeet.unimeetbackend.domain.common.Major;
import site.unimeet.unimeetbackend.domain.common.Mbti;
import site.unimeet.unimeetbackend.domain.user.User;
import site.unimeet.unimeetbackend.domain.user.UserRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class InitDB {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        ArrayList<Major> majors = new ArrayList<>();
        majors.add(Major.AI);
        User user = new User("김성김", "nickname", "eeee@email.com", passwordEncoder.encode("pppp"), Gender.MALE, Mbti.ENFJ, majors, Department.IT);
        userRepository.save(user);
    }
}
