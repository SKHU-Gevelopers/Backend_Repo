package site.unimeet.unimeetbackend.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // json 직렬화 시 한글 설명 desc로 반환됨

public enum Gender {
    FEMALE("여성"), MALE("남성"), NONE("무관")
    ;
    private final String desc;

    Gender(String desc) {
        this.desc = desc;
    }
    // 역직렬화를 위함. CategoryConverter 에서 사용.
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static Gender from(String major){
        return Gender.valueOf(major.toUpperCase());
    }
}
