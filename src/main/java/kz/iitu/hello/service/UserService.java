package kz.iitu.hello.service;

import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.repository.UsersRepository;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.web.converter.UserConverter;
import kz.iitu.hello.web.dto.form.UserFormDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final UserConverter userConverter;

    public List<UserGridDto> findAllView() {
        return usersRepository.findAll().stream().map(userConverter::toGridDto).toList();
    }

    public UserFormDto getForm(Long id) {
        return id == null ? new UserFormDto() : userConverter.toFormDto(findById(id));
    }

    public void create(UserFormDto form) {
        User user = new User();
        userConverter.applyFormToEntity(form, user);
        usersRepository.save(user);
    }

    public void update(Long id, UserFormDto form) {
        User user = findById(id);
        userConverter.applyFormToEntity(form, user);
        usersRepository.save(user);
    }

    public void delete(Long id) {
        usersRepository.delete(findById(id));
    }

    public User findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
