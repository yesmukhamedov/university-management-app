package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {

    Page<User> findByUserNameContainingIgnoreCaseAndEmailContainingIgnoreCase(String userName, String email, Pageable pageable);
}
