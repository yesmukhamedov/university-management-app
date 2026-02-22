package kz.iitu.hello.repository;

import kz.iitu.hello.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachersRepository extends JpaRepository<Teacher, Long> {
}
