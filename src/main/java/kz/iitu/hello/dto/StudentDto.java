package kz.iitu.hello.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentDto {
    private Long id;
    private String studentName;
    private Integer age;
    private Double gpa;
    private String groupName;
    private Long userId;
    private String userName;
    private List<Long> courseIds;
    private List<String> courseNames;
}
