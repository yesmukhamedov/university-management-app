package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {

    Page<User> findByUserNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
            String username,
            String email,
            Pageable pageable
    );

    Page<User> findByUserNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndRole(
            String username,
            String email,
            UserRole role,
            Pageable pageable
    );
}
