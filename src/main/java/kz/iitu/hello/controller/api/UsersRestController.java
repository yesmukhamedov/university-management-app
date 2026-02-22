package kz.iitu.hello.controller.api;

import kz.iitu.hello.UserFormDto;
import kz.iitu.hello.UserGridDto;
import kz.iitu.hello.service.UserService;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserGridDto> read() { return userService.findAllView(); }

    @PostMapping
    public void create(@RequestBody UserFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        userService.validateUserForm(form, br, null);
        if (br.hasErrors()) {
            throw new IllegalArgumentException(br.getFieldError().getDefaultMessage());
        }
        userService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody UserFormDto form) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(form, "form");
        userService.validateUserForm(form, br, id);
        if (br.hasErrors()) {
            throw new IllegalArgumentException(br.getFieldError().getDefaultMessage());
        }
        userService.update(id, form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { userService.delete(id); }
}
