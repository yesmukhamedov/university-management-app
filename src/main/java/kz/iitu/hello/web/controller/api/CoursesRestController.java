package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.service.CourseService;
import kz.iitu.hello.web.dto.form.CourseFormDto;
import kz.iitu.hello.web.dto.search.CourseSearchForm;
import kz.iitu.hello.web.dto.view.CourseViewDto;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.CourseFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesRestController {
    private final CourseService courseService;
    private final CourseFormValidator courseFormValidator;

    @GetMapping
    public Page<CourseViewDto> read(CourseSearchForm form,
                                    @PageableDefault(size = 10) Pageable pageable) {
        return courseService.search(form, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CourseFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        courseFormValidator.validate(form, br, null);
        BindingResultValidationUtils.validate(br);
        courseService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CourseFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        courseFormValidator.validate(form, br, id);
        BindingResultValidationUtils.validate(br);
        courseService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
