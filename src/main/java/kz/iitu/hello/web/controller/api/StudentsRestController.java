package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.service.StudentService;
import kz.iitu.hello.web.dto.form.StudentFormDto;
import kz.iitu.hello.web.dto.search.StudentSearchForm;
import kz.iitu.hello.web.dto.view.StudentViewDto;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.StudentFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsRestController {
    private final StudentService studentService;
    private final StudentFormValidator studentFormValidator;

    @GetMapping
    public Page<StudentViewDto> read(StudentSearchForm form,
                                     @PageableDefault(size = 10) Pageable pageable) {
        return studentService.search(form, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody StudentFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        studentFormValidator.validate(form, br, null);
        BindingResultValidationUtils.validate(br);
        studentService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody StudentFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        studentFormValidator.validate(form, br, id);
        BindingResultValidationUtils.validate(br);
        studentService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
