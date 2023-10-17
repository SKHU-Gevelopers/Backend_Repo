package site.unimeet.unimeetbackend.domain.student.component.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Department {
    IT("IT융합자율학부"), HUMANITIES("인문융합자율학부"),
    SOCIAL("사회융합자율학부"), MEDIA("미디어콘텐츠학부"),
    NONE("비공개")

    ;

    @JsonValue
    private final String desc;

    Department(String desc) {
        this.desc = desc;
    }

    // 역직렬화를 위함. Department Converter에서 사용.
    @JsonCreator
    public static Department from(String department){
        return Department.valueOf(department.toUpperCase());
    }
}