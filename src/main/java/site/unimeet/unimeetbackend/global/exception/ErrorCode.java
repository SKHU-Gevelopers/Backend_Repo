package site.unimeet.unimeetbackend.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 이메일 인증
    EMAIL_VERIFICATION_CODE_MISMATCHED(400, "이메일 인증 코드가 일치하지 않습니다."),
    EMAIL_CANNOT_BE_SENT(500, "이메일을 보낼 수 없습니다."),
    EMAIL_VERIFICATION_CODE_NOT_FOUND(400, "이메일 인증 코드가 존재하지 않습니다."),
    // 회원가입
    EMAIL_ALREADY_REGISTERED(409, "이미 가입된 이메일입니다."),

    // 인증 - 로그인 시도
    MISMATCHED_SIGNIN_INFO(400, "잘못된 로그인 정보입니다."),

    // 인증 - 토큰
    NOT_EXISTS_AUTHORIZATION(401, "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(401, "인증 타입이 Bearer 타입이 아닙니다."),
    ACCESS_TOKEN_EXPIRED(401, "해당 access token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(401, "tokenType이 access token이 아닙니다."),
    REFRESH_TOKEN_EXPIRED(401, "해당 refresh token은 만료됐습니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "해당 refresh token은 존재하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효하지 않은 토큰입니다."),

    // 파일
    FILE_CANNOT_BE_STORED(500, "파일을 저장할 수 없습니다."),
    FILE_CANNOT_BE_READ(500, "파일을 읽을 수 없습니다."),
    FILE_CANNOT_BE_SENT(500, "읽어들인 파일을 전송할 수 없습니다"),

    // 학생
    STUDENT_NOT_FOUND(404, "해당 학생을 찾을 수 없습니다."),

    // 게시글
    POST_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),
    ;




    private int status;
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}