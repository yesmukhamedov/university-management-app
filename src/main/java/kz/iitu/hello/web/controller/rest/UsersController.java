package kz.iitu.hello.web.controller.rest;

import kz.iitu.hello.domain.enums.UserRole;
import kz.iitu.hello.service.UserService;
import kz.iitu.hello.web.dto.form.UserFormDto;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.UserFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;
    private final UserFormValidator userFormValidator;

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        model.addAttribute("editMode", id != null);
        model.addAttribute("form", userService.getForm(id));
        model.addAttribute("users", userService.findAllView());
        model.addAttribute("roles", UserRole.values());
        return "users";
    }

    @PostMapping
    public String create(@ModelAttribute("form") UserFormDto form, BindingResult bindingResult, Model model) {
        userFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            return renderFormWithErrors(model, form, false);
        }
        userService.create(form);
        return "redirect:/users";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") UserFormDto form, BindingResult bindingResult, Model model) {
        userFormValidator.validate(form, bindingResult, form.getId());
        if (BindingResultValidationUtils.hasErrors(bindingResult)) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }
        userService.update(id, form);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    private String renderFormWithErrors(Model model, UserFormDto form, boolean editMode) {
        model.addAttribute("editMode", editMode);
        model.addAttribute("users", userService.findAllView());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("form", form);
        return "users";
    }
}
