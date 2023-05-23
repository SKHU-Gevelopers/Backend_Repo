package site.unimeet.unimeetbackend.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import site.unimeet.unimeetbackend.domain.student.enums.Department;

@Component
public class DepartmentConverter implements Converter<String, Department> {
    @Override
    public Department convert(String source) {
        return Department.from(source);
    }
}
