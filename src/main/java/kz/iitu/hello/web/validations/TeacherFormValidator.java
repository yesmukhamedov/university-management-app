package kz.iitu.hello.web.validations;

import kz.iitu.hello.web.dto.form.TeacherFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class TeacherFormValidator {

    public void validate(TeacherFormDto form, BindingResult bindingResult, Long currentId) {
        // Bean Validation annotations handle Teacher form constraints.
    }
}
