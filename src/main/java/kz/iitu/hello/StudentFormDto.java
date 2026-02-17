package kz.iitu.hello;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class StudentFormDto {

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

    public StudentFormDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
}
