package site.unimeet.unimeetbackend.domain.meetup;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MeetUp extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String contact;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post targetPost; // 소속 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Student sender; // 신청자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Student receiver; // 피신청자

    @Builder
    private MeetUp(String title, String content, String contact, List<String> imageUrls, Post targetPost, Student sender, Student receiver) {
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.imageUrls = imageUrls;
        this.targetPost = targetPost;
        this.sender = sender;
        this.receiver = receiver;

        // 신청자와 피신청자가 같다면 예외발생
        if (sender.getId().equals(receiver.getId())) {
            throw new BusinessException(ErrorCode.MEETUP_CANNOT_BE_MADE_WITH_SAME_STUDENT);
        }
    }

    public void checkAuthority(String httpRequesterEmail) {
        String receiverEmail = receiver.getEmail();
        String senderEmail = sender.getEmail();

        // sender도 receiver도 아니라면 예외 발생
        if (!
                (receiverEmail.equals(httpRequesterEmail) || senderEmail.equals(httpRequesterEmail))
        ) {
            throw new BusinessException(ErrorCode.MEETUP_NOT_RECEIVER_OR_SENDER);
        }
    }
}















