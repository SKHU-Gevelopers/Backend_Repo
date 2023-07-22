package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import lombok.Builder;
import lombok.Getter;
import site.unimeet.unimeetbackend.api.student.dto.StudentIdAndNickNameDto;
import site.unimeet.unimeetbackend.domain.student.component.dm.DM;

// DM 단건 조회
public class ReadDMDto {
    @Builder
    @Getter
    public static class Res {
        private DMDto dm;
        private StudentIdAndNickNameDto sender;

        public static Res from(DM dm) {
            DMDto dmDto = DMDto.builder()
                    .title(dm.getTitle())
                    .content(dm.getContent())
                    .build();
            StudentIdAndNickNameDto sender = StudentIdAndNickNameDto.from(dm.getSender());

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
