package site.unimeet.unimeetbackend.api.post.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.meetup.MeetUp;

import java.util.List;

public class MeetUpDetailDto {
    @Getter
    public static class Res {
        MeetUpDto meetUp;

        public static Res from(MeetUp meetUp) {
            StudentIdAndNickNameDto sender = StudentIdAndNickNameDto.from(meetUp.getSender());
            MeetUpDto meetUpDto = MeetUpDto.builder()
                    .title(meetUp.getTitle())
                    .content(meetUp.getContent())
                    .meetUpImages(meetUp.getImageUrls())
                    .sender(sender)
                    .targetPostId(meetUp.getTargetPost().getId())
                    .build();
            return new Res(meetUpDto);
        }

        private Res(MeetUpDto MeetUpDto) {
            this.meetUp = MeetUpDto;
        }

        @Getter
        @Builder
        private static class MeetUpDto{
            String title;
            String content;
            List<String> meetUpImages;
            StudentIdAndNickNameDto sender;
            Long targetPostId;
        }
    }

}
