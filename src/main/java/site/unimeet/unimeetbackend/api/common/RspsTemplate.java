package site.unimeet.unimeetbackend.api.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 응답 템플릿
// 응답 DTO가 리스트가 아닐 때
@Getter
public class RspsTemplate<T> {
    private int statusCode;
    private T data;

    public RspsTemplate(HttpStatus httpStatus, T data) {
        this.statusCode = httpStatus.value();
        this.data = data;
    }
}