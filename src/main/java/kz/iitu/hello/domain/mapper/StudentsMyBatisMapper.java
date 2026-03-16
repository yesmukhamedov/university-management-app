package kz.iitu.hello.domain.mapper;

import kz.iitu.hello.domain.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentsMyBatisMapper {

    @Select("""
            <script>
            SELECT id, student_name AS studentName, age, group_name AS groupName, gpa
            FROM students
            WHERE 1 = 1
              <if test='name != null and name != ""'>
                AND LOWER(student_name) LIKE LOWER(CONCAT('%', #{name}, '%'))
              </if>
              <if test='gpaFrom != null'>
                AND gpa <![CDATA[ >= ]]> #{gpaFrom}
              </if>
              <if test='gpaTo != null'>
                AND gpa <![CDATA[ <= ]]> #{gpaTo}
              </if>
            ORDER BY gpa DESC, student_name ASC
            </script>
            """)
    List<Student> findStudents(@Param("name") String name,
                               @Param("gpaFrom") Double gpaFrom,
                               @Param("gpaTo") Double gpaTo);
}
