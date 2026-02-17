package kz.iitu.hello;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class TeacherFormDto {

    @NotBlank
    @Size(min = 2, max = 100)
    private String teacherName;

    @NotNull
    @Min(0)
    @Max(60)
    private Integer experienceYears;

    @NotNull
    private Department department;

    @NotNull
    @Positive
    private Long userId;
}
