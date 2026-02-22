package kz.iitu.hello.web.dto.view;

import java.util.List;

public class StudentViewDto {

    private Long id;
    private String studentName;
    private Integer age;
    private String groupName;
    private Double gpa;
    private UserGridDto user;
    private List<CourseGridDto> courses;

    public StudentViewDto() {
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
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
