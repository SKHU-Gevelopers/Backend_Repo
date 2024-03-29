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
    NOT_EXISTS_AUTH_HEADER(401, "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_TYPE(401, "인증 타입이 Bearer 타입이 아닙니다."),
    ACCESS_TOKEN_EXPIRED(401, "해당 access token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(401, "tokenType이 access token이 아닙니다."),
    INVALID_REFRESH_TOKEN(401, "잘못된 refresh token입니다. 토큰을 재발급할 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "해당 refresh token은 존재하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    FORBIDDEN(403, "권한이 없습니다."),

    // 파일
    FILE_CANNOT_BE_STORED(500, "파일을 저장할 수 없습니다."),
    FILE_CANNOT_BE_READ(500, "파일을 읽을 수 없습니다."),
    FILE_CANNOT_BE_SENT(500, "읽어들인 파일을 전송할 수 없습니다"),

    // 학생
    STUDENT_NOT_FOUND(404, "해당 학생을 찾을 수 없습니다."),

    // 쪽지
    DM_NOT_FOUND(404, "해당 쪽지를 찾을 수 없습니다."),
    DM_RECEIVER_NOT_MATCHED(403,"해당 쪽지의 수신자가 아닙니다."),
    DM_NOT_RECEIVER_OR_SENDER(403, "해당 쪽지를 조회할 권한이 없습니다"),

    // 게시글
    POST_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),
    POST_ALREADY_DONE(400, "해당 게시글은 만남신청이 종료되었습니다."),
    POST_WRITER_NOT_MATCHED(400, "해당 게시글의 작성자가 아닙니다."),
    
    // 댓글
    NOT_EXIST_COMMENT(404, "해당 댓글을 찾을 수 없습니다."),
    COMMENT_WRITER_NOT_MATCHED(400,"해당 댓글의 작성자가 아닙니다."),

    // 만남(MeetUp)
    MEETUP_NOT_FOUND(404, "해당 만남을 찾을 수 없습니다."),
    MEETUP_NOT_RECEIVER_OR_SENDER(403, "해당 만남을 조회할 권한이 없습니다"),
    MEETUP_CANNOT_BE_MADE_WITH_SAME_STUDENT(400, "신청자와 피신청자가 같습니다."),
    MEETUP_SENDER_DUPLICATED(409, "한 게시글에 중복 신청할 수 없습니다."),


    KAKAO_ID_ALREADY_REGISTERED(409, "이미 가입된 카카오 아이디입니다."),

    STUDENT_CONSTRAINT_ERROR(400, "저장 실패. 중복 값 혹은 Null 값 등 제약 조건을 확인해주세요."),
    ;

    private int status;
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}