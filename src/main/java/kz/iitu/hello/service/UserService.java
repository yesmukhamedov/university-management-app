package kz.iitu.hello.service;

import kz.iitu.hello.web.dto.form.UserFormDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    public List<UserGridDto> findAllView() {
        return usersRepository.findAll().stream().map(this::toGridDto).toList();
    }

    public UserFormDto getForm(Long id) {
        return id == null ? new UserFormDto() : toFormDto(findById(id));
    }

    public void create(UserFormDto form) {
        User user = new User();
        applyFormToEntity(form, user);
        usersRepository.save(user);
    }

    public void update(Long id, UserFormDto form) {
        User user = findById(id);
        applyFormToEntity(form, user);
        usersRepository.save(user);
    }

    public void delete(Long id) {
        usersRepository.delete(findById(id));
    }

    public void validateUserForm(UserFormDto form, BindingResult bindingResult, Long currentUserId) {
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

    public User findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
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
