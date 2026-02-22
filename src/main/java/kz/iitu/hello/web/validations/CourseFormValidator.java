package kz.iitu.hello.web.validations;

import kz.iitu.hello.web.dto.form.CourseFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class CourseFormValidator {

    public void validate(CourseFormDto form, BindingResult bindingResult, Long currentId) {
        if (form.getCourseName() == null || form.getCourseName().isBlank()) {
            bindingResult.rejectValue("courseName", "courseName.blank", "Course name is required");
        }
        if (form.getCredits() == null) {
            bindingResult.rejectValue("credits", "credits.blank", "Credits is required");
        } else if (form.getCredits() < 1 || form.getCredits() > 10) {
            bindingResult.rejectValue("credits", "credits.invalid", "Credits must be between 1 and 10");
        }
        if (form.getMaxStudents() == null) {
            bindingResult.rejectValue("maxStudents", "maxStudents.blank", "Max students is required");
        } else if (form.getMaxStudents() < 1) {
            bindingResult.rejectValue("maxStudents", "maxStudents.invalid", "Max students must be positive");
        }
        if (form.getTeacherId() == null) {
            bindingResult.rejectValue("teacherId", "teacherId.blank", "Teacher is required");
        }
    }
}
