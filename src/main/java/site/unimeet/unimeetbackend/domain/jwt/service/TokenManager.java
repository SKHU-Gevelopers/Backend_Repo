package site.unimeet.unimeetbackend.domain.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.jwt.constant.AuthScheme;
import site.unimeet.unimeetbackend.domain.jwt.constant.TokenType;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.NotValidTokenException;

import java.security.Key;
import java.util.Date;


@Slf4j
@Component
public class TokenManager {

    private final long accessTokenExpMillis;
    private final long refreshTokenExpMillis;
    private final Key key;

    @Autowired
    public TokenManager(
            @Value("${token.secret}") String tokenSecret
            , @Value("${token.access-token-expiration-time}") long accessTokenExpMillis
            , @Value("${token.refresh-token-expiration-time}") long refreshTokenExpMillis) {
        // Base64 Decode. String to Bin
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpMillis = accessTokenExpMillis;
        this.refreshTokenExpMillis = refreshTokenExpMillis;
    }

    public TokenDto createTokenDto(String email) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(email, accessTokenExpireTime);
        String refreshToken = createRefreshToken(email, refreshTokenExpireTime);
        return TokenDto.builder()
                .authScheme(AuthScheme.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExp(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExp(refreshTokenExpireTime)
                .build();
    }

    private Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + accessTokenExpMillis);
    }

    private Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + refreshTokenExpMillis);
    }

    private String createAccessToken(String email, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())                // 토큰 제목
                .setAudience(email)                                 // 토큰 대상자
                .setIssuedAt(new Date())                         // 토큰 발급 시간
                .setExpiration(expirationTime)                // 토큰 만료 시간
                /**
                 *      Claim 에는 Standard Claims 들을 제외하고도
                 *      key-value 로 여러 값 저장 가능.
                 */
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    private String createRefreshToken(String email, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())               // 토큰 제목
                .setAudience(email)                                 // 토큰 대상자
                .setIssuedAt(new Date())                            // 토큰 발급 시간
                .setExpiration(expirationTime)                      // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    // Claims 파싱과 예외처리를 담당함.
    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new NotValidTokenException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    public String getMemberEmail(String jws) {
        return getTokenClaims(jws).getAudience(); // aud == email
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token);
            return true;
            /* 검증 여부를 boolean 반환해야 하므로 예외상황에서 로그만 출력함.*/
        } catch (MalformedJwtException e) {
            log.info("잘못된 jwt token", e);
        } catch (JwtException e) {
            log.info("jwt token 검증 중 에러 발생", e);
        }
        return false;
    }

    public boolean isTokenExpired(Date tokenExpiredTime) {
        Date now = new Date();
        return now.after(tokenExpiredTime);
    }

    public String getTokenType(String token){
        Claims claims = getTokenClaims(token);
        return claims.getSubject();
    }
}