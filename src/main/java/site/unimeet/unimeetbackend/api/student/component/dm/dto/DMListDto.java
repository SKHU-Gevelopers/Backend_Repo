package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.DM;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DMListDto {
    @Getter
    public static class Res {
        private List<DMDto> dmList;

        public Res(List<DM> dmList) {
            List<DMDto> convertedDMDtos = dmList.stream()
                    .map(DMDto::from)
                    .collect(Collectors.toList());

            this.dmList = convertedDMDtos;
        }

        @Getter
        @Builder
        private static class DMDto {
            private String title;
            private StudentIdAndNickNameDto sender;
            @JsonFormat(pattern = "yy-MM-dd HH:mm")
            private LocalDateTime sentAt;

            private static DMDto from(DM dm) {
                StudentIdAndNickNameDto sender = StudentIdAndNickNameDto.from(dm.getSender());

                DMDto dmDto = DMDto.builder()
                        .title(dm.getTitle())
                        .sender(sender)
                        .sentAt(dm.getCreateTime())
                        .build();
                return dmDto;
            }
        }
    }
}
