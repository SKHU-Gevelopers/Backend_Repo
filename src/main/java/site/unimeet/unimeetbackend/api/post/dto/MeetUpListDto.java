package site.unimeet.unimeetbackend.api.post.dto;

import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.meetup.MeetUp;
import site.unimeet.unimeetbackend.domain.student.Student;

import java.util.List;
import java.util.stream.Collectors;

public class MeetUpListDto {
    @Getter
    public static class Res {
        List<MeetUpDto> meetUps;

        public static Res from(List<MeetUp> meetUps) {
            List<MeetUpDto> meetUpDtos = meetUps.stream()
                    .map(MeetUpDto::new)
                    .collect(Collectors.toList());
            return new Res(meetUpDtos);
        }

        private Res(List<MeetUpDto> meetUps) {
            this.meetUps = meetUps;
        }

        @Getter
        private static class MeetUpDto {
            Long id;
            String title;
            StudentIdAndNickNameDto sender;

            public MeetUpDto(MeetUp meetUp) {
                this.id = meetUp.getId();
                this.title = meetUp.getTitle();
                Student requester = meetUp.getSender();
                this.sender = StudentIdAndNickNameDto.from(requester);
            }
        }
    }
}
