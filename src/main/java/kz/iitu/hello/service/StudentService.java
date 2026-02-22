package kz.iitu.hello.service;

import kz.iitu.hello.web.dto.form.StudentFormDto;
import kz.iitu.hello.web.dto.grid.CourseGridDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.web.dto.view.StudentViewDto;
import kz.iitu.hello.domain.entity.Course;
import kz.iitu.hello.domain.entity.Student;
import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.domain.repository.CoursesRepository;
import kz.iitu.hello.domain.repository.StudentsRepository;
import kz.iitu.hello.domain.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StudentService {
    private final StudentsRepository studentRepository;
    private final UsersRepository userRepository;
    private final CoursesRepository courseRepository;

    public StudentService(StudentsRepository studentRepository, UsersRepository userRepository, CoursesRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public List<StudentViewDto> findAllView() { return studentRepository.findAll().stream().map(this::toViewDto).toList(); }
    public List<UserGridDto> findAllUsers() { return userRepository.findAll().stream().map(this::toUserGridDto).toList(); }
    public List<CourseGridDto> findAllCourses() { return courseRepository.findAll().stream().map(this::toCourseGridDto).toList(); }
    public StudentFormDto getForm(Long id) { return id == null ? new StudentFormDto() : toFormDto(findById(id)); }

    public void create(StudentFormDto form) { Student s = new Student(); applyFormToEntity(form, s); studentRepository.save(s); }
    public void update(Long id, StudentFormDto form) { Student s = findById(id); applyFormToEntity(form, s); studentRepository.save(s); }
    public void delete(Long id) {
        Student student = findById(id);
        if (student.getCourses() != null) {
            for (Course course : student.getCourses()) {
                if (course.getStudents() != null) course.getStudents().remove(student);
            }
            student.getCourses().clear();
        }
        studentRepository.delete(student);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private void applyFormToEntity(StudentFormDto form, Student student) {
        student.setStudentName(form.getStudentName());
        student.setAge(form.getAge());
        student.setGpa(form.getGpa());
        student.setGroupName(form.getGroupName());
        student.setUser(findUser(form.getUserId()));
        List<Long> courseIds = form.getCourseIds();
        Set<Course> courses = courseIds == null ? new HashSet<>() : new HashSet<>(courseRepository.findAllById(courseIds));
        student.setCourses(courses);
    }

    private StudentFormDto toFormDto(Student student) {
        StudentFormDto dto = new StudentFormDto();
        dto.setId(student.getId()); dto.setStudentName(student.getStudentName()); dto.setAge(student.getAge()); dto.setGpa(student.getGpa()); dto.setGroupName(student.getGroupName());
        if (student.getUser() != null) dto.setUserId(student.getUser().getId());
        if (student.getCourses() != null) dto.setCourseIds(student.getCourses().stream().map(Course::getId).toList());
        return dto;
    }

    private StudentViewDto toViewDto(Student student) {
        StudentViewDto dto = new StudentViewDto();
        dto.setId(student.getId()); dto.setStudentName(student.getStudentName()); dto.setAge(student.getAge()); dto.setGpa(student.getGpa()); dto.setGroupName(student.getGroupName());
        if (student.getUser() != null) dto.setUser(toUserGridDto(student.getUser()));
        List<CourseGridDto> courseDtos = new ArrayList<>();
        if (student.getCourses() != null) for (Course course : student.getCourses()) courseDtos.add(toCourseGridDto(course));
        dto.setCourses(courseDtos);
        return dto;
    }

    private UserGridDto toUserGridDto(User user) { if (user == null) return null; UserGridDto dto = new UserGridDto(); dto.setId(user.getId()); dto.setUserName(user.getUserName()); dto.setEmail(user.getEmail()); dto.setRole(user.getRole()); return dto; }
    private CourseGridDto toCourseGridDto(Course course) { if (course == null) return null; CourseGridDto dto = new CourseGridDto(); dto.setId(course.getId()); dto.setCourseName(course.getCourseName()); dto.setCredits(course.getCredits()); dto.setMaxStudents(course.getMaxStudents()); return dto; }
}
