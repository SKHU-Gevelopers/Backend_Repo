package site.unimeet.unimeetbackend.domain.meetup;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;

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
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post targetPost;          // 소속 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Student requester; // 신청자

    @Builder
    private MeetUp(String title, String content, List<String> imageUrls, Post targetPost, Student requester) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.targetPost = targetPost;
        this.requester = requester;
    }
}















