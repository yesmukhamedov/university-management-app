package kz.iitu.hello.controller.mvc;

import kz.iitu.hello.dto.form.CourseFormDto;
import kz.iitu.hello.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    private final CourseService courseService;

    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        model.addAttribute("editMode", id != null);
        model.addAttribute("form", courseService.getForm(id));
        model.addAttribute("courses", courseService.findAllView());
        model.addAttribute("teachers", courseService.findAllTeachers());
        model.addAttribute("students", courseService.findAllStudents());
        return "courses";
    }

    @PostMapping
    public String create(@ModelAttribute("form") CourseFormDto form, BindingResult bindingResult, Model model) {
        courseService.validateCourseForm(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return renderFormWithErrors(model, form, false);
        }
        courseService.create(form);
        return "redirect:/courses";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") CourseFormDto form, BindingResult bindingResult, Model model) {
        courseService.validateCourseForm(form, bindingResult);
        if (bindingResult.hasErrors()) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }
        courseService.update(id, form);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        courseService.delete(id);
        return "redirect:/courses";
    }

    private String renderFormWithErrors(Model model, CourseFormDto form, boolean editMode) {
        model.addAttribute("editMode", editMode);
        model.addAttribute("form", form);
        model.addAttribute("courses", courseService.findAllView());
        model.addAttribute("teachers", courseService.findAllTeachers());
        model.addAttribute("students", courseService.findAllStudents());
        return "courses";
    }
}
