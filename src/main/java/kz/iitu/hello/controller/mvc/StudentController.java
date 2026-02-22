package kz.iitu.hello.controller.mvc;

import jakarta.validation.Valid;
import kz.iitu.hello.dto.StudentCreateDto;
import kz.iitu.hello.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        StudentCreateDto form = id == null ? new StudentCreateDto() : studentService.findFormById(id);
        populateModel(model, form, id != null);
        return "students";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") StudentCreateDto form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            populateModel(model, form, false);
            return "students";
        }

        studentService.create(form);
        return "redirect:/students";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") StudentCreateDto form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            form.setId(id);
            populateModel(model, form, true);
            return "students";
        }

        studentService.update(id, form);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }

    private void populateModel(Model model, StudentCreateDto form, boolean editMode) {
        model.addAttribute("form", form);
        model.addAttribute("editMode", editMode);
        model.addAttribute("students", studentService.findAllEntities());
        model.addAttribute("users", studentService.findAllUsers());
        model.addAttribute("courses", studentService.findAllCourses());
    }
}
