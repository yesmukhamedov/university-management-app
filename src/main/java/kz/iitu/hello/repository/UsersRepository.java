package kz.iitu.hello.repository;

import kz.iitu.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
}
