package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.web.dto.form.CourseFormDto;
import kz.iitu.hello.web.dto.view.CourseViewDto;
import kz.iitu.hello.service.CourseService;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CoursesRestController {
    private final CourseService courseService;

    public CoursesRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseViewDto> read() { return courseService.findAllView(); }

    @PostMapping
    public void create(@RequestBody CourseFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        courseService.validateCourseForm(form, br);
        BindingResultValidationUtils.validate(br);
        courseService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CourseFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        courseService.validateCourseForm(form, br);
        BindingResultValidationUtils.validate(br);
        courseService.update(id, form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { courseService.delete(id); }
}
