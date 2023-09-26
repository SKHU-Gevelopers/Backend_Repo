package site.unimeet.unimeetbackend.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.student.component.enums.Major;

import java.util.Arrays;

@Component
public class MajorConverter implements Converter<String, Major> {
    @Override
    public Major convert(String source) {
        return Arrays.stream(Major.values())
                .filter(major -> major.getDesc().equals(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value. No Matching Enum Constant. your input value ->  " + source));
    }
}
