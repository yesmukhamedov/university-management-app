package kz.iitu.hello;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseGridDto {
    private Long id;

    private String courseName;

    private Integer credits;

    private Integer maxStudents;
}
