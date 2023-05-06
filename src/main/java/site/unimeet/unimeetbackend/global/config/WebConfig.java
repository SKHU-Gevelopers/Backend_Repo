package site.unimeet.unimeetbackend.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.unimeet.unimeetbackend.global.converter.CategoryConverter;
import site.unimeet.unimeetbackend.global.converter.DepartmentConverter;
import site.unimeet.unimeetbackend.global.converter.GenderConverter;
import site.unimeet.unimeetbackend.global.converter.MajorConverter;
import site.unimeet.unimeetbackend.global.interceptor.AuthenticationInterceptor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CategoryConverter categoryConverter; private final DepartmentConverter departmentConverter;
    private final MajorConverter majorConverter; private final GenderConverter genderConverter;
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
                .excludePathPatterns("/health")  // 이 경로는 검사 제외
        ;
    }
}
