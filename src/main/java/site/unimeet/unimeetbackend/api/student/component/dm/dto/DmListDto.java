package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentProfileDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.Dm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DmListDto {
    @Getter
    public static class Res {
        private List<DmDto> dmList;

        public Res(List<Dm> dmList) {
            this.dmList = dmList.stream()
                    .map(DmDto::from)
                    .collect(Collectors.toList());
        }

        @Getter
        @Builder
        private static class DmDto {
            long id;
            private String title;
            private StudentProfileDto sender;
            private StudentProfileDto receiver;
            @JsonFormat(pattern = "yy-MM-dd HH:mm")
            private LocalDateTime sentAt;

            private static DmDto from(Dm dm) {
                StudentProfileDto sender = StudentProfileDto.from(dm.getSender());
                StudentProfileDto receiver = StudentProfileDto.from(dm.getReceiver());

                return DmDto.builder()
                        .id(dm.getId())
                        .title(dm.getTitle())
                        .sender(sender)
                        .receiver(receiver)
                        .sentAt(dm.getCreateTime())
                        .build();
            }
        }
    }
}
