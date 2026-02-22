package kz.iitu.hello.web.controller.rest;

import jakarta.validation.Valid;
import kz.iitu.hello.domain.enums.Department;
import kz.iitu.hello.service.TeacherService;
import kz.iitu.hello.web.dto.form.TeacherFormDto;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.TeacherFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeachersController {
    private final TeacherService teacherService;
    private final TeacherFormValidator teacherFormValidator;

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        model.addAttribute("editMode", id != null);
        model.addAttribute("form", teacherService.getForm(id));
        model.addAttribute("teachers", teacherService.findAllView());
        model.addAttribute("users", teacherService.findAllUsers());
        model.addAttribute("courses", teacherService.findAllCourses());
        model.addAttribute("departments", Department.values());
        return "teachers";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TeacherFormDto form, BindingResult bindingResult, Model model) {
        teacherFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            return renderFormWithErrors(model, form, false);
        }
        teacherService.create(form);
        return "redirect:/teachers";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") TeacherFormDto form, BindingResult bindingResult, Model model) {
        teacherFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }
        teacherService.update(id, form);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    private String renderFormWithErrors(Model model, TeacherFormDto form, boolean editMode) {
        model.addAttribute("editMode", editMode);
        model.addAttribute("form", form);
        model.addAttribute("teachers", teacherService.findAllView());
        model.addAttribute("users", teacherService.findAllUsers());
        model.addAttribute("courses", teacherService.findAllCourses());
        model.addAttribute("departments", Department.values());
        return "teachers";
    }
}
