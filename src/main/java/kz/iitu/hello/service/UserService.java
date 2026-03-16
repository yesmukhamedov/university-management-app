package kz.iitu.hello.service;

import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.repository.UsersRepository;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.web.converter.UserConverter;
import kz.iitu.hello.web.dto.form.UserFormDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.web.dto.search.UserSearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private static final String DEFAULT_SORT = "createdAt";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "userName", "email", "createdAt", "role");

    private final UsersRepository usersRepository;
    private final UserConverter userConverter;

    public List<UserGridDto> findAllView() {
        return usersRepository.findAll().stream().map(userConverter::toGridDto).toList();
    }

    public List<UserGridDto> findAllView(String username) {
        String usernameFilter = username == null ? "" : username;
        return usersRepository.findByUserNameContainingIgnoreCaseOrderByCreatedAtDesc(usernameFilter)
                .stream()
                .map(userConverter::toGridDto)
                .toList();
    }

    public Page<UserGridDto> search(UserSearchForm form, Pageable pageable) {
        String userNameFilter = form.getUsername() == null ? "" : form.getUsername();
        String emailFilter = form.getEmail() == null ? "" : form.getEmail();

        Sort.Direction direction = form.getSortDirection() == null ? Sort.Direction.ASC : form.getSortDirection();
        String requestedSortBy = form.getSortBy() == null ? DEFAULT_SORT : form.getSortBy();
        String sortBy = ALLOWED_SORT_FIELDS.contains(requestedSortBy) ? requestedSortBy : DEFAULT_SORT;

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));

        return usersRepository
                .findByUserNameContainingIgnoreCaseAndEmailContainingIgnoreCase(userNameFilter, emailFilter, sortedPageable)
                .map(userConverter::toGridDto);
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
