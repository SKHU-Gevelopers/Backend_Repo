package site.unimeet.unimeetbackend.domain.auth.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {
    @Getter // executorService 에는 Getter가 필요하지 않을 것으로 판단.
    private final Cache<String, String> codeExpirationCache; // <email, verification code>. thread-safe map.

    @Autowired
    public EmailVerificationService() {
        codeExpirationCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public String generateCode(String email) {
        // 4자리 검증 코드 생성 후 캐시에 저장
        String verificationCode = UUID.randomUUID().toString().substring(0, 4);
        codeExpirationCache.put(email, verificationCode);

        return verificationCode;
    }
}
