package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Student, Long> {
}
