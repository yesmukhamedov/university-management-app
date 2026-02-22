package kz.iitu.hello.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class StudentCreateDto {
    private Long id;

    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 50, message = "Student name must be between 2 and 50 characters")
    private String studentName;

    @NotNull(message = "Age is required")
    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    @NotNull(message = "GPA is required")
    @DecimalMin(value = "0.0", message = "GPA must be at least 0.0")
    @DecimalMax(value = "4.0", message = "GPA must be at most 4.0")
    private Double gpa;

    @NotBlank(message = "Group name is required")
    private String groupName;

    @NotNull(message = "User is required")
    private Long userId;

    private List<Long> courseIds;
}
