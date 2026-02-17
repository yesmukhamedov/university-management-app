package kz.iitu.hello;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherGridDto {

    private Long id;

    private String teacherName;

    private Integer experienceYears;

    private Department department;
}
