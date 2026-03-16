package kz.iitu.hello.web.validations;

import kz.iitu.hello.web.dto.form.StudentFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class StudentFormValidator {

    public void validate(StudentFormDto form, BindingResult bindingResult, Long currentId) {
        // Bean Validation annotations handle Student form constraints.
    }
}
