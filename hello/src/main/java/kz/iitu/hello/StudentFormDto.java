package kz.iitu.hello;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFormDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private String studentName;

    @Min(16)
    @Max(100)
    private Integer age;

    @DecimalMin("0.0")
    @DecimalMax("4.0")
    private Double gpa;

    @NotBlank
    private String groupName;

    @NotNull
    private Long userId;
}
