package site.unimeet.unimeetbackend.domain.student.component.dm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DMService {
    private final DMRepository dmRepository;
    private final StudentService studentService;

    @Transactional
    public void sendDM(Long senderId, String receiverEmail, String title, String content) {
        Student receiver = studentService.findByEmail(receiverEmail);
        Student sender = studentService.findById(senderId);

        DM dm = DM.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .receiver(receiver)
                .build();

        dmRepository.save(dm);
    }


}
