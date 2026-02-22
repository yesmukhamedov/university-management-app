package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.web.dto.form.UserFormDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.service.UserService;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.UserFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersRestController {
    private final UserService userService;
    private final UserFormValidator userFormValidator;


    @GetMapping
    public List<UserGridDto> read() { return userService.findAllView(); }

    @PostMapping
    public void create(@RequestBody UserFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        userFormValidator.validate(form, br, null);
        BindingResultValidationUtils.validate(br);
        userService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody UserFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        userFormValidator.validate(form, br, id);
        BindingResultValidationUtils.validate(br);
        userService.update(id, form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { userService.delete(id); }
}
