package site.unimeet.unimeetbackend.api.post.dto;


import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.post.Post;
import site.unimeet.unimeetbackend.domain.post.enums.State;
import site.unimeet.unimeetbackend.domain.student.Student;
import site.unimeet.unimeetbackend.domain.student.component.enums.Gender;

import java.util.List;

// 게시글 상세정보 조회
public class PostDetailDto {
    @Getter
    @Builder
    public static class Res {
        private String title;
        private String content;
        private List<String> imageUrls;
        private State state; // 만남 모집중 상태. In progress, Done
        private Integer maxPeople; // 희망 인원
        private Gender gender; // 희망 성별
        private Integer likes; // 좋아요 수

        private String profileImageUrl; //작성자 프로필 사진
        private String nickname; //작성자 닉네임

        public static PostDetailDto.Res from(Post post){
            Student writer = post.getWriter();
            return Res.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .imageUrls(post.getImageUrls())
                    .state(post.getState())
                    .maxPeople(post.getMaxPeople())
                    .gender(post.getGender())
                    .likes(post.getLikes())
                    .profileImageUrl(writer.getProfileImageUrl())
                    .nickname(writer.getNickname())
                    .build();
        }
    }
}
