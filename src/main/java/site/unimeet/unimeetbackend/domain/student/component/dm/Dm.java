package site.unimeet.unimeetbackend.domain.student.component.dm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;

import javax.persistence.*;

/** Direct Message. 쪽지 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Dm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Student sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Student receiver;

    @Builder
    private Dm(String title, String content, Student sender, Student receiver) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void checkReceiverDm(String email) {
        String receiverEmail = receiver.getEmail();

        // receiver와 id가 같지 않다면 예외발생
        if (! email.equals(receiverEmail)) {
            throw new BusinessException(ErrorCode.DM_RECEIVER_NOT_MATCHED);
        }
    }
}













