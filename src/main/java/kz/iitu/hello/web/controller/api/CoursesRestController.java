package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.web.dto.form.CourseFormDto;
import kz.iitu.hello.web.dto.view.CourseViewDto;
import kz.iitu.hello.service.CourseService;
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
        if (br.hasErrors()) throw new IllegalArgumentException(br.getFieldError().getDefaultMessage());
        courseService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody CourseFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        courseService.validateCourseForm(form, br);
        if (br.hasErrors()) throw new IllegalArgumentException(br.getFieldError().getDefaultMessage());
        courseService.update(id, form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { courseService.delete(id); }
}
