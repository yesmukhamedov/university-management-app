package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachersRepository extends JpaRepository<Teacher, Long> {
}
