package site.unimeet.unimeetbackend.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.unimeet.unimeetbackend.global.converter.CategoryConverter;
import site.unimeet.unimeetbackend.global.converter.DepartmentConverter;
import site.unimeet.unimeetbackend.global.converter.GenderConverter;
import site.unimeet.unimeetbackend.global.converter.MajorConverter;
import site.unimeet.unimeetbackend.global.interceptor.AuthenticationInterceptor;
import site.unimeet.unimeetbackend.global.resolver.StudentEmailArgResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Converters
    private final CategoryConverter categoryConverter; private final DepartmentConverter departmentConverter;
    private final MajorConverter majorConverter; private final GenderConverter genderConverter;
    // Resolvers
    private final StudentEmailArgResolver studentEmailArgResolver;

    private final AuthenticationInterceptor authenticationInterceptor;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(categoryConverter);
        registry.addConverter(departmentConverter);
        registry.addConverter(majorConverter);
        registry.addConverter(genderConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .order(1)   // 인증 인터셉터를 첫 번째로 수행
                .addPathPatterns("/**")     // 이 경로를 대상으로 동작
                .excludePathPatterns("/auth/**", "/users/sign-up", "/test")  // 이 경로는 검사 제외
        ;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(studentEmailArgResolver);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Todo Origin 웹서버 도메인으로
                .allowedOriginPatterns("*") // Request Header의 Origin을, Response Header의 Access-Control-Allow-Origin에 그대로 넣어준다.
                .allowedMethods(HttpMethod.GET.name()
                        ,HttpMethod.POST.name()
                        ,HttpMethod.PATCH.name()
                        ,HttpMethod.PUT.name()
                        ,HttpMethod.DELETE.name()
                        , HttpMethod.OPTIONS.name()
                )
                .allowCredentials(true)
                .maxAge(1800)
        // maxage 만큼 preflight 캐싱은 기본값이 1800sec(30m), 즉 Access-Control-Max-Age=1800
        ;
    }
}

















