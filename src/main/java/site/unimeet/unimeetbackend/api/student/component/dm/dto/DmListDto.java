package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.Dm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DmListDto {
    @Getter
    public static class Res {
        private List<DmDto> dmList;

        public Res(List<Dm> dmList) {
            List<DmDto> convertedDmDtos = dmList.stream()
                    .map(DmDto::from)
                    .collect(Collectors.toList());

            this.dmList = convertedDmDtos;
        }

        @Getter
        @Builder
        private static class DmDto {
            long id;
            private String title;
            private StudentIdAndNickNameDto sender;
            private StudentIdAndNickNameDto receiver;
            private String senderProfileImageUrl;
            @JsonFormat(pattern = "yy-MM-dd HH:mm")
            private LocalDateTime sentAt;

            private static DmDto from(Dm dm) {
                StudentIdAndNickNameDto sender = StudentIdAndNickNameDto.from(dm.getSender());
                StudentIdAndNickNameDto receiver = StudentIdAndNickNameDto.from(dm.getReceiver());

                DmDto dmDto = DmDto.builder()
                        .id(dm.getId())
                        .title(dm.getTitle())
                        .sender(sender)
                        .receiver(receiver)
                        .sentAt(dm.getCreateTime())
                        .senderProfileImageUrl(dm.getSender().getProfileImageUrl())
                        .build();
                return dmDto;
            }
        }
    }
}
