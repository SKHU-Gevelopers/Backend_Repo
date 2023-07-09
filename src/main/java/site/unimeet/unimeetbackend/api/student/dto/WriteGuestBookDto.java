package site.unimeet.unimeetbackend.api.student.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class WriteGuestBookDto {

    @NoArgsConstructor
    @Getter
    public static class Req {
        public String content;
    }
}
