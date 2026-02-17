package kz.iitu.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        UserFormDto form = id != null ? toFormDto(findUser(id)) : new UserFormDto();
        model.addAttribute("editMode", id != null);
        model.addAttribute("form", form);
        model.addAttribute("users", usersRepository.findAll().stream().map(this::toGridDto).toList());
        model.addAttribute("roles", UserRole.values());
        return "users";
    }

    @PostMapping
    public String create(@ModelAttribute("form") UserFormDto form, BindingResult bindingResult, Model model) {
        validateUserForm(form, bindingResult, null);
        if (bindingResult.hasErrors()) {
            return renderFormWithErrors(model, form, false);
        }

        User user = new User();
        applyFormToEntity(form, user);
        usersRepository.save(user);
        return "redirect:/users";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") UserFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        User user = findUser(id);
        validateUserForm(form, bindingResult, id);
        if (bindingResult.hasErrors()) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }

        applyFormToEntity(form, user);
        usersRepository.save(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        User user = findUser(id);
        usersRepository.delete(user);
        return "redirect:/users";
    }

    private String renderFormWithErrors(Model model, UserFormDto form, boolean editMode) {
        model.addAttribute("editMode", editMode);
        model.addAttribute("users", usersRepository.findAll().stream().map(this::toGridDto).toList());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("form", form);
        return "users";
    }

    private void validateUserForm(UserFormDto form, BindingResult bindingResult, Long currentUserId) {
        if (form.getUserName() == null || form.getUserName().isBlank()) {
            bindingResult.rejectValue("userName", "userName.blank", "User name is required");
        }
        if (form.getEmail() == null || form.getEmail().isBlank()) {
            bindingResult.rejectValue("email", "email.blank", "Email is required");
        } else if (!form.getEmail().contains("@")) {
            bindingResult.rejectValue("email", "email.invalid", "Email must contain @");
        }
        if (currentUserId == null && (form.getPassword() == null || form.getPassword().isBlank())) {
            bindingResult.rejectValue("password", "password.blank", "Password is required");
        } else if (form.getPassword() != null && !form.getPassword().isBlank() && form.getPassword().length() < 6) {
            bindingResult.rejectValue("password", "password.length", "Password must be at least 6 characters");
        }
        if (form.getRole() == null) {
            bindingResult.rejectValue("role", "role.blank", "Role is required");
        }

        List<User> users = usersRepository.findAll();
        for (User existingUser : users) {
            if (currentUserId != null && existingUser.getId().equals(currentUserId)) {
                continue;
            }
            if (form.getUserName() != null && form.getUserName().equalsIgnoreCase(existingUser.getUserName())) {
                bindingResult.rejectValue("userName", "userName.duplicate", "User with this username already exists");
            }
            if (form.getEmail() != null && form.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
                bindingResult.rejectValue("email", "email.duplicate", "User with this email already exists");
            }
        }
    }

    private User findUser(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private void applyFormToEntity(UserFormDto form, User user) {
        user.setUserName(form.getUserName());
        user.setEmail(form.getEmail());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(form.getPassword());
        }
        user.setRole(form.getRole());
    }

    private UserFormDto toFormDto(User user) {
        UserFormDto dto = new UserFormDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private UserGridDto toGridDto(User user) {
        UserGridDto dto = new UserGridDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
