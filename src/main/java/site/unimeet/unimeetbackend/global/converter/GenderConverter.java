package site.unimeet.unimeetbackend.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.common.Gender;

@Component
public class GenderConverter implements Converter<String, Gender> {

    @Override
    public Gender convert(String source) {
        return Gender.from(source);
    }
}
