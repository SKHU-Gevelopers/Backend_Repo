package site.unimeet.unimeetbackend.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.common.Category;

@Component
public class CategoryConverter implements Converter<String, Category> {
    @Override
    public Category convert(String source) {
        return Category.from(source);
    }
}
