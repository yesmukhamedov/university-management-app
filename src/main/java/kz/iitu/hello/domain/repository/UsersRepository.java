package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
}
