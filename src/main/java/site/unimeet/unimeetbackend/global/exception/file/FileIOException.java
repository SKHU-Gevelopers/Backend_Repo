package site.unimeet.unimeetbackend.global.exception.file;


import site.unimeet.unimeetbackend.global.exception.BusinessException;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;

public class FileIOException extends BusinessException {
    public FileIOException(ErrorCode errorCode) {
        super(errorCode);
    }
}
