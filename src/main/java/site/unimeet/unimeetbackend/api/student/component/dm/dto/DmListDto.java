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
            private String title;
            private StudentIdAndNickNameDto sender;
            @JsonFormat(pattern = "yy-MM-dd HH:mm")
            private LocalDateTime sentAt;

            private static DmDto from(Dm dm) {
                StudentIdAndNickNameDto sender = StudentIdAndNickNameDto.from(dm.getSender());

                DmDto dmDto = DmDto.builder()
                        .title(dm.getTitle())
                        .sender(sender)
                        .sentAt(dm.getCreateTime())
                        .build();
                return dmDto;
            }
        }
    }
}