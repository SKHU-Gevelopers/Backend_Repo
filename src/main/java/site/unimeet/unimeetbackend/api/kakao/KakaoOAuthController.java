package site.unimeet.unimeetbackend.api.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import site.unimeet.unimeetbackend.api.common.ResTemplate;
import site.unimeet.unimeetbackend.domain.jwt.dto.TokenDto;
import site.unimeet.unimeetbackend.domain.student.StudentService;

import java.util.Map;

@Slf4j
@RestController
public class KakaoOAuthController {

    private final String GRANT_TYPE = "authorization_code";
    private final StudentService studentService;
    @Value("${kakao.restapi-key}")
    private String clientId;

    public KakaoOAuthController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<ResTemplate<TokenDto>> kakaoCallback(
            @RequestParam("code") String code
    ) throws JsonProcessingException {
        // code 발급 시 https://kauth.kakao.com/oauth/authorize 여기로
        // code, clinetId, response_type, scope 등을 보내야 하는데, 이를 JS SDK에서 보내주는 듯 함. Rdir URI는 Code밖에 없음.
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", "https://skhu-unimeet.site/auth/kakao/callback");
        body.add("code", code);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        KakaoToken response = restTemplate.postForObject(url, request, KakaoToken.class);

        String[] splitedIdToken = response.getIdToken().split("\\.");

        String idTokenPayload = splitedIdToken[1];

        byte[] payloadBytes = Base64Utils.decodeFromUrlSafeString(idTokenPayload);

        String payload = new String(payloadBytes);

        ObjectMapper objMapper = new ObjectMapper();
        Map<String, String> map = objMapper.readValue(payload, Map.class);
        String email = map.get("email");
        String sub = map.get("sub");
        String iss = map.get("iss");
        log.warn("email = {}, sub = {}, iss = {}", email, sub, iss);

        TokenDto tokenDto = studentService.oAuthSignIn(sub);

        if (tokenDto.isFirstSignIn()) {
            ResTemplate<TokenDto> resTemplate = new ResTemplate<>(HttpStatus.CREATED, "첫 로그인, 사용자 회원가입 후 로그인 처리", tokenDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resTemplate);
        } else {
            ResTemplate<TokenDto> resTemplate = new ResTemplate<>(HttpStatus.OK, "기존 회원, 로그인 성공", tokenDto);
            return ResponseEntity.status(HttpStatus.OK).body(resTemplate);
        }
    }
}
