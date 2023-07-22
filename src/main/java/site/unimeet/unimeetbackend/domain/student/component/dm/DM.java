package site.unimeet.unimeetbackend.domain.student.component.dm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.student.Student;

import javax.persistence.*;

/** Direct Message. 쪽지 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DM extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String Title;
    @Column(nullable = false, length = 1000)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Student sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Student receiver;
}













