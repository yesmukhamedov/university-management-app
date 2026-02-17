package kz.iitu.hello;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentViewDto {

    private Long id;

    private String studentName;

    private Integer age;

    private String groupName;

    private Double gpa;

    private UserGridDto user;

    private List<CourseGridDto> courses;
}
