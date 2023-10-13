package site.unimeet.unimeetbackend.api.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import site.unimeet.unimeetbackend.domain.jwt.service.TokenManager;

import java.util.Map;

@Slf4j
@RestController
public class KakaoOAuthController {

    private final String GRANT_TYPE = "authorization_code";
    @Value("${kakao.private}")
    private String clientId;
    private final TokenManager tokenManager;

    public KakaoOAuthController(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @GetMapping("/auth/kakao/callback")
    public KakaoToken kakaoCallback(
            @RequestParam("code") String code
    ) throws JsonProcessingException {
        // code 발급 시 https://kauth.kakao.com/oauth/authorize 여기로
        // code, clinetId, response_type, scope 등을 보내야 하는데, 이를 JS SDK에서 보내주는 듯 함. Rdir URI는 Code밖에 없음.
        System.out.println("code = " + code);
        // rest api key 9f84ae0fe7d28dcc3c1a6edf1a7e9807

        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", "https://unimeet.duckdns.org/auth/kakao/callback");
        body.add("code", code);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        KakaoToken response = restTemplate.postForObject(url, request, KakaoToken.class);

        String[] splitedIdToken = response.getIdToken().split("\\.");
        String idTokenHeader = splitedIdToken[0];
        String idTokenPayload = splitedIdToken[1];
        String idTokenSignature = splitedIdToken[2];

        log.info("idTokenHeader = {}", idTokenHeader);
        log.info("idTokenPayload = {}", idTokenPayload);
        log.info("idTokenSignature = {}", idTokenSignature);

        byte[] headerBytes = Base64Utils.decodeFromUrlSafeString(idTokenHeader);
        byte[] payloadBytes = Base64Utils.decodeFromUrlSafeString(idTokenPayload);
        byte[] signatureBytes = Base64Utils.decodeFromUrlSafeString(idTokenSignature);

        String header = new String(headerBytes);
        String payload = new String(payloadBytes);
        String signature = new String(signatureBytes);

        ObjectMapper objMapper = new ObjectMapper();
        Map<String, String> map = objMapper.readValue(payload, Map.class);
        String email = map.get("email");
        String sub = map.get("sub");
        String iss = map.get("iss");
        log.warn("email = {}, sub = {}, iss = {} ", email, sub, iss);


        response.setIdTokenHeader(header);
        response.setIdTokenPayload(payload);
        response.setIdTokenSignature(signature);
        return response;
    }
}
