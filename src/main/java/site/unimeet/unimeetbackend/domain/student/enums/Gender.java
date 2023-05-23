package site.unimeet.unimeetbackend.domain.student.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    FEMALE("여성"), MALE("남성"), NONE("무관")
    ;
    @JsonValue
    private final String desc;

    Gender(String desc) {
        this.desc = desc;
    }
    // 역직렬화를 위함. CategoryConverter 에서 사용.
    public static Gender from(String major){
        return Gender.valueOf(major.toUpperCase());
    }
}
