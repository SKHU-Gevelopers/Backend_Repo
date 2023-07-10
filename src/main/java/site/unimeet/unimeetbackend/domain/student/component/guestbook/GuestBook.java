package site.unimeet.unimeetbackend.domain.student.component.guestbook;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.student.Student;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GuestBook extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // 방명록 작성자. 한 작성자가 방명록 여러개 작성 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Student writer;

    // 방명록 작성대상 Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_student_id")
    private Student targetStudent;

    @Builder
    public GuestBook(String content, Student writer, Student targetStudent) {
        this.content = content;
        this.writer = writer;
        this.targetStudent = targetStudent;
    }
}












