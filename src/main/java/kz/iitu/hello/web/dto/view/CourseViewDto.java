package kz.iitu.hello.web.dto.view;

import java.util.List;

public class CourseViewDto {

    private Long id;
    private String courseName;
    private Integer credits;
    private Integer maxStudents;
    private TeacherGridDto teacher;
    private List<StudentGridDto> students;

    public CourseViewDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public TeacherGridDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherGridDto teacher) {
        this.teacher = teacher;
    }

    public List<StudentGridDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentGridDto> students) {
        this.students = students;
    }
}
