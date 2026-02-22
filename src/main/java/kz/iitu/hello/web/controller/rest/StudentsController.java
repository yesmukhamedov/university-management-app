package kz.iitu.hello.web.controller.rest;

import jakarta.validation.Valid;
import kz.iitu.hello.service.StudentService;
import kz.iitu.hello.web.dto.form.StudentFormDto;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.StudentFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentsController {
    private final StudentService studentService;
    private final StudentFormValidator studentFormValidator;

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
        studentFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            return renderFormWithErrors(model, form, false);
        }
        studentService.create(form);
        return "redirect:/students";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") StudentFormDto form, BindingResult bindingResult, Model model) {
        studentFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
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
