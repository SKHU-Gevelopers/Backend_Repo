package site.unimeet.unimeetbackend.api.student.component.dm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class SendDMDto {
    @Getter
    @NoArgsConstructor
    public static class Req{
        @NotBlank(message = "쪽지 제목을 입력해주세요.")
        private String title;
        @NotBlank(message = "쪽지 내용을 입력해주세요.")
        @Length(max = 1000)
        private String content;
    }
}

