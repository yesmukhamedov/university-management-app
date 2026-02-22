package kz.iitu.hello.controller.mvc;

import jakarta.validation.Valid;
import kz.iitu.hello.dto.form.StudentFormDto;
import kz.iitu.hello.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentsController {
    private final StudentService studentService;

    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        model.addAttribute("form", studentService.getForm(id));
        model.addAttribute("editMode", id != null);
        model.addAttribute("students", studentService.findAllView());
        model.addAttribute("users", studentService.findAllUsers());
        model.addAttribute("courses", studentService.findAllCourses());
        return "students";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") StudentFormDto form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return renderFormWithErrors(model, form, false);
        }
        studentService.create(form);
        return "redirect:/students";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") StudentFormDto form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }
        studentService.update(id, form);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }

    private String renderFormWithErrors(Model model, StudentFormDto form, boolean editMode) {
        model.addAttribute("form", form);
        model.addAttribute("editMode", editMode);
        model.addAttribute("students", studentService.findAllView());
        model.addAttribute("users", studentService.findAllUsers());
        model.addAttribute("courses", studentService.findAllCourses());
        return "students";
    }
}
