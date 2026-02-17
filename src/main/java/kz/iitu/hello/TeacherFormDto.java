package kz.iitu.hello;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class TeacherFormDto {

    private Long id;

    @NotBlank(message = "Teacher name is required")
    @Size(min = 2, max = 100, message = "Teacher name must be between 2 and 100 characters")
    private String teacherName;

    @NotNull(message = "Experience years is required")
    @Min(value = 0, message = "Experience years cannot be negative")
    @Max(value = 60, message = "Experience years must be at most 60")
    private Integer experienceYears;

    @NotNull(message = "Department is required")
    private Department department;

    @NotNull(message = "User is required")
    private Long userId;

    private List<Long> courseIds;

    public TeacherFormDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
