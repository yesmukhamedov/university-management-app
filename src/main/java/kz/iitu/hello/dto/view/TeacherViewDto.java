package kz.iitu.hello.dto.view;

import java.util.List;

public class TeacherViewDto {

    private Long id;
    private String teacherName;
    private Integer experienceYears;
    private Department department;
    private UserGridDto user;
    private List<CourseGridDto> courses;

    public TeacherViewDto() {
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

    public UserGridDto getUser() {
        return user;
    }

    public void setUser(UserGridDto user) {
        this.user = user;
    }

    public List<CourseGridDto> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseGridDto> courses) {
        this.courses = courses;
    }
}
