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

        public Res(List<MeetUp> meetUps) {
            this.meetUps = meetUps.stream()
                    .map(MeetUpDto::new)
                    .collect(Collectors.toList());
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
