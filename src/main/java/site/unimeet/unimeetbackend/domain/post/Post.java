package site.unimeet.unimeetbackend.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.unimeet.unimeetbackend.domain.common.BaseTimeEntity;
import site.unimeet.unimeetbackend.domain.student.enums.Gender;
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
    private int maxPeople; // 희망 인원
    @Column(nullable = false)
    private Gender gender; // 희망 성별

    private int likes; // 좋아요 수

    @Builder
    public Post(String title, String content, List<String> imageUrls, State state, int maxPeople, Gender gender) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.state = state;
        this.maxPeople = maxPeople;
        this.gender = gender;
    }
}


















