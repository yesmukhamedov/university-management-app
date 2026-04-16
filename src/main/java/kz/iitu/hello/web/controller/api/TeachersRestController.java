package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.service.TeacherService;
import kz.iitu.hello.web.dto.form.TeacherFormDto;
import kz.iitu.hello.web.dto.search.TeacherSearchForm;
import kz.iitu.hello.web.dto.view.TeacherViewDto;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.TeacherFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private final TeacherService teacherService;
    private final TeacherFormValidator teacherFormValidator;

    @GetMapping
    public Page<TeacherViewDto> read(TeacherSearchForm form,
                                     @PageableDefault(size = 10) Pageable pageable) {
        return teacherService.search(form, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody TeacherFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        teacherFormValidator.validate(form, br, null);
        BindingResultValidationUtils.validate(br);
        teacherService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody TeacherFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        teacherFormValidator.validate(form, br, id);
        BindingResultValidationUtils.validate(br);
        teacherService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
