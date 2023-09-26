package site.unimeet.unimeetbackend.domain.jwt.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.unimeet.unimeetbackend.domain.jwt.constant.AuthScheme;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthException;

@RequiredArgsConstructor
@Service
public class TokenValidator {
    public void validateBearer(String authHeader) {
        //  1. 토큰 유무 확인
        if(!StringUtils.hasText(authHeader)){
            throw new AuthException(ErrorCode.NOT_EXISTS_AUTH_HEADER);
        }

        //  2. authorization Bearer 체크
        String[] authorizations = authHeader.split(" ");
        // AuthScheme.BEARER.getType() 은 "Bearer"문자열 반환
        if(authorizations.length < 2 || (!AuthScheme.BEARER.getType().equals(authorizations[0]))){
            throw new AuthException(ErrorCode.NOT_VALID_BEARER_TYPE);
        }
    }
}
