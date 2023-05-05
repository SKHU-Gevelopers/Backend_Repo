package site.unimeet.unimeetbackend.global.exception.auth;

import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;

public class NotValidTokenException extends BusinessException {

    public NotValidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}