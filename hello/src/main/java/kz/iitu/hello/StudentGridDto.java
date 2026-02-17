package kz.iitu.hello;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentGridDto {

    private Long id;

    private String studentName;

    private Integer age;

    private String groupName;

    private Double gpa;
}
