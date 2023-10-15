package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentProfileDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.Dm;

import java.time.LocalDateTime;

// DM 단건 조회
public class ReadDMDto {
    @AllArgsConstructor
    @Getter
    public static class Res {
        private DMDto dm;

        public static Res from(Dm dm) {
            StudentProfileDto sender = StudentProfileDto.from(dm.getSender());
            DMDto dmDto = DMDto.builder()
                    .title(dm.getTitle())
                    .content(dm.getContent())
                    .sender(sender)
                    .sentAt(dm.getCreateTime())
                    .build();

            return new Res(dmDto);
        }

        @Getter
        @Builder
        private static class DMDto {
            private String title;
            private String content;
            private StudentProfileDto sender;
            @JsonFormat(pattern = "yy-MM-dd HH:mm")
            private LocalDateTime sentAt;
        }
    }
}
