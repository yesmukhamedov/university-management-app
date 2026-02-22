package kz.iitu.hello.web.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseFormDto {

    private Long id;
    private String courseName;
    private Integer credits;
    private Integer maxStudents;
    private Long teacherId;
    private List<Long> studentIds;
}
