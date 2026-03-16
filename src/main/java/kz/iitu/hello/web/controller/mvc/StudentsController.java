package kz.iitu.hello.web.controller.mvc;

import jakarta.validation.Valid;
import kz.iitu.hello.service.StudentService;
import kz.iitu.hello.web.dto.form.StudentFormDto;
import kz.iitu.hello.web.dto.search.StudentSearchForm;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.StudentFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class StudentsController {
    private final StudentService studentService;
    private final StudentFormValidator studentFormValidator;

    @GetMapping("/manage/students")
    public String read(@RequestParam(name = "id", required = false) Long id,
                       @ModelAttribute("searchForm") StudentSearchForm searchForm,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {
        model.addAttribute("form", studentService.getForm(id));
        model.addAttribute("editMode", id != null);
        fillCommonAttributes(model, searchForm, pageable);
        return "students";
    }

    @GetMapping("/students")
    public String listStudents(@ModelAttribute("searchForm") StudentSearchForm form,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        model.addAttribute("form", studentService.getForm(null));
        model.addAttribute("editMode", false);
        fillCommonAttributes(model, form, pageable);
        return "students";
    }

    @PostMapping("/manage/students")
    public String create(@Valid @ModelAttribute("form") StudentFormDto form, BindingResult bindingResult, Model model) {
        studentFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            return renderFormWithErrors(model, form, false);
        }
        studentService.create(form);
        return "redirect:/manage/students";
    }

    @PutMapping("/manage/students/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") StudentFormDto form, BindingResult bindingResult, Model model) {
        studentFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }
        studentService.update(id, form);
        return "redirect:/manage/students";
    }

    @DeleteMapping("/manage/students/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/manage/students";
    }

    private String renderFormWithErrors(Model model, StudentFormDto form, boolean editMode) {
        model.addAttribute("form", form);
        model.addAttribute("editMode", editMode);
        fillCommonAttributes(model, new StudentSearchForm(), Pageable.ofSize(10));
        return "students";
    }

    private void fillCommonAttributes(Model model, StudentSearchForm form, Pageable pageable) {
        model.addAttribute("page", studentService.search(form, pageable));
        model.addAttribute("searchForm", form);
        model.addAttribute("users", studentService.findAllUsers());
        model.addAttribute("courses", studentService.findAllCourses());
    }
}
