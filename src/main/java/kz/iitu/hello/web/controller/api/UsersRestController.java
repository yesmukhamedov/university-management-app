package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.service.UserService;
import kz.iitu.hello.web.dto.form.UserFormDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.web.dto.search.UserSearchForm;
import kz.iitu.hello.web.validations.BindingResultValidationUtils;
import kz.iitu.hello.web.validations.UserFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersRestController {
    private final UserService userService;
    private final UserFormValidator userFormValidator;

    @GetMapping
    public Page<UserGridDto> read(UserSearchForm searchForm,
                                  @PageableDefault(size = 10) Pageable pageable) {
        return userService.search(searchForm, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
