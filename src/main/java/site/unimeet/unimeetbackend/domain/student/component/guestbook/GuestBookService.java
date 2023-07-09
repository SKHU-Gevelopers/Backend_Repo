package site.unimeet.unimeetbackend.domain.student.component.guestbook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.StudentService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GuestBookService {
    private final GuestBookRepository guestBookRepository;
    private final StudentService studentService;

    @Transactional
    public GuestBook write(Long targetUserId, String content, String writerEmail) {

        Student writerStudent = studentService.findByEmail(writerEmail);// 작성자
        Student targetStudent = studentService.findById(targetUserId);// 방명록을 남기려는 대상

        GuestBook guestBook = GuestBook.builder()
                .content(content)
                .writer(writerStudent)
                .targetStudent(targetStudent)
                .build();
        return guestBookRepository.save(guestBook);
    }
}
