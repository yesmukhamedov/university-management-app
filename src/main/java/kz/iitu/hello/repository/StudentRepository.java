package kz.iitu.hello.repository;

import kz.iitu.hello.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
