package site.unimeet.unimeetbackend.domain.jwt.constant;

import lombok.Getter;

// Http Auth 헤더에 사용될 인증 방식
@Getter
public enum AuthScheme {

    BEARER("Bearer");

    AuthScheme(String type) {
        this.type = type;
    }

    private String type;
}