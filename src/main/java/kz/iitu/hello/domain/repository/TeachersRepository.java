package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.Teacher;
import kz.iitu.hello.domain.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeachersRepository extends JpaRepository<Teacher, Long> {

    @Query("""
            SELECT t FROM Teacher t
            WHERE (:department IS NULL OR t.department = :department)
              AND (:name IS NULL OR LOWER(t.teacherName) LIKE LOWER(CONCAT('%', :name, '%')))
            ORDER BY t.teacherName ASC
            """)
    List<Teacher> searchTeachers(@Param("department") Department department,
                                 @Param("name") String name);
}
