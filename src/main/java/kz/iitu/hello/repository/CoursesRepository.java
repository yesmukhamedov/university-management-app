package kz.iitu.hello.repository;

import kz.iitu.hello.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Course, Long> {
}
