package site.unimeet.unimeetbackend.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.unimeet.unimeetbackend.global.interceptor.AuthenticationInterceptor;
import site.unimeet.unimeetbackend.global.resolver.StudentIdArgResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Resolvers
    private final StudentIdArgResolver studentIdArgResolver;

    private final AuthenticationInterceptor authenticationInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .order(1)   // 인증 인터셉터를 첫 번째로 수행
                .addPathPatterns("/**")     // 이 경로를 대상으로 동작
                .excludePathPatterns("/**, /auth/**", "/users/sign-up", "/token/reissue", "/auth/kakao/callback", "/index.html", "/push")  // 이 경로는 검사 제외
        ;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(studentIdArgResolver);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Todo Origin 웹서버 도메인으로
                .allowedOriginPatterns("*") // Request Header의 Origin을, Response Header의 Access-Control-Allow-Origin에 그대로 넣어준다.
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
        // maxage 만큼 preflight 캐싱은 기본값이 1800sec(30m), 즉 Access-Control-Max-Age=1800
        ;
    }
}

















