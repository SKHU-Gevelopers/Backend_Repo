package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNameDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.DM;

public class ReadDMDto {
    @Builder
    @Getter
    public static class Res {
        private DMDto dm;
        private StudentIdAndNameDto sender;

        public static Res from(DM dm) {
            DMDto dmDto = DMDto.builder()
                    .title(dm.getTitle())
                    .content(dm.getContent())
                    .build();
            StudentIdAndNameDto sender = StudentIdAndNameDto.from(dm.getSender());

            return Res.builder()
                    .dm(dmDto)
                    .sender(sender)
                    .build();
        }

        @Getter
        @Builder
        private static class DMDto {
            private String title;
            private String content;
        }
    }
}
