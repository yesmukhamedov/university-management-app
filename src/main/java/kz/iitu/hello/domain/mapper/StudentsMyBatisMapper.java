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
              <if test='groupName != null and groupName != ""'>
                AND LOWER(group_name) LIKE LOWER(CONCAT('%', #{groupName}, '%'))
              </if>
              <if test='ageFrom != null'>
                AND age <![CDATA[ >= ]]> #{ageFrom}
              </if>
              <if test='ageTo != null'>
                AND age <![CDATA[ <= ]]> #{ageTo}
              </if>
              <if test='gpaFrom != null'>
                AND gpa <![CDATA[ >= ]]> #{gpaFrom}
              </if>
              <if test='gpaTo != null'>
                AND gpa <![CDATA[ <= ]]> #{gpaTo}
              </if>
            ORDER BY ${sortBy} ${sortDirection}
            LIMIT #{limit}
            OFFSET #{offset}
            </script>
            """)
    List<Student> findStudents(@Param("name") String name,
                               @Param("groupName") String groupName,
                               @Param("ageFrom") Integer ageFrom,
                               @Param("ageTo") Integer ageTo,
                               @Param("gpaFrom") Double gpaFrom,
                               @Param("gpaTo") Double gpaTo,
                               @Param("sortBy") String sortBy,
                               @Param("sortDirection") String sortDirection,
                               @Param("limit") int limit,
                               @Param("offset") long offset);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM students
            WHERE 1 = 1
              <if test='name != null and name != ""'>
                AND LOWER(student_name) LIKE LOWER(CONCAT('%', #{name}, '%'))
              </if>
              <if test='groupName != null and groupName != ""'>
                AND LOWER(group_name) LIKE LOWER(CONCAT('%', #{groupName}, '%'))
              </if>
              <if test='ageFrom != null'>
                AND age <![CDATA[ >= ]]> #{ageFrom}
              </if>
              <if test='ageTo != null'>
                AND age <![CDATA[ <= ]]> #{ageTo}
              </if>
              <if test='gpaFrom != null'>
                AND gpa <![CDATA[ >= ]]> #{gpaFrom}
              </if>
              <if test='gpaTo != null'>
                AND gpa <![CDATA[ <= ]]> #{gpaTo}
              </if>
            </script>
            """)
    long countStudents(@Param("name") String name,
                       @Param("groupName") String groupName,
                       @Param("ageFrom") Integer ageFrom,
                       @Param("ageTo") Integer ageTo,
                       @Param("gpaFrom") Double gpaFrom,
                       @Param("gpaTo") Double gpaTo);
}
