package site.unimeet.unimeetbackend.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;
import site.unimeet.unimeetbackend.domain.post.enums.State;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Lob
    private String content;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> imageUrls;
    @Enumerated(EnumType.STRING)
    private State state; // 만남 모집중 상태. In progress, Done
    @Column(nullable = false)
    private Integer maxPeople; // 희망 인원
    @Column(nullable = false)
    private Gender gender; // 희망 성별
    private Integer likes; // 좋아요 수

    @Builder
    private Post(String title, String content, List<String> imageUrls, int maxPeople, Gender gender) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.state = State.IN_PROGRESS;
        this.maxPeople = maxPeople;
        this.gender = gender;
        this.likes = 0;
    }

    public void update(String title, String content, int maxPeople, Gender gender){
        this.title = title;
        this.content = content;
//        this.imageUrls = imageUrls;
        this.maxPeople = maxPeople;
        this.gender = gender;
    }

    public void increaseLikeCount() {
        this.likes += 1;
    }

    public void decreaseLikeCount() {
        this.likes -= 1;
    }

}


















