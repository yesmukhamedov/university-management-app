package kz.iitu.hello;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseViewDto {

    private Long id;

    private String courseName;

    private Integer credits;

    private Integer maxStudents;

    private TeacherGridDto teacher;

    private List<StudentGridDto> students;
}
