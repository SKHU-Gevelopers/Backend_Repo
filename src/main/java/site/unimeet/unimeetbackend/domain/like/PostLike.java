package site.unimeet.unimeetbackend.domain.like;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.student.Student;

import javax.persistence.*;

/**
 *  "내가 좋아요 한 게시글" 을 추려내기 위해 필요한 클래스
 *  Student 와 Post 의 ManyToMany 관계를 풀어낸다.
 *
 *  좋아요 버튼이 눌리면 Post의 likes 카운트를 하나 늘리고, PostLike 객체를 생성한다.
 *  좋아요 버튼이 다시 눌리면 Post의 likes 카운트를 하나 줄이고, PostLike 객체를 삭제한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private boolean status; //true = 좋아요, false = 좋아요 취소

    @Builder
    public PostLike(Post post, Student student) {
        this.student = student;
        this.post = post;
        this.status = true;
    }


}

















