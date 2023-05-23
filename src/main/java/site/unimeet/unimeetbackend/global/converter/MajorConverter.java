package site.unimeet.unimeetbackend.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.student.enums.Major;

@Component
public class MajorConverter implements Converter<String, Major> {
    @Override
    public Major convert(String source) {
        return Major.from(source);
    }
}
