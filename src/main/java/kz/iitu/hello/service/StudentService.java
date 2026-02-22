package kz.iitu.hello.service;

import kz.iitu.hello.*;
import kz.iitu.hello.dto.StudentCreateDto;
import kz.iitu.hello.dto.StudentDto;
import kz.iitu.hello.entity.Student;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository repository;
    private final UsersRepository userRepository;
    private final CoursesRepository courseRepository;

    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<Student> findAllEntities() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public StudentDto findById(Long id) {
        return toDto(findEntityById(id));
    }

    public StudentDto create(StudentCreateDto createDto) {
        Student student = new Student();
        apply(createDto, student);
        return toDto(repository.save(student));
    }

    public StudentDto update(Long id, StudentCreateDto createDto) {
        Student student = findEntityById(id);
        apply(createDto, student);
        return toDto(repository.save(student));
    }

    public void delete(Long id) {
        Student student = findEntityById(id);
        if (student.getCourses() != null) {
            for (Course course : student.getCourses()) {
                if (course.getStudents() != null) {
                    course.getStudents().remove(student);
                }
            }
            student.getCourses().clear();
        }
        repository.delete(student);
    }

    @Transactional(readOnly = true)
    public List<UserGridDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::toUserGridDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CourseGridDto> findAllCourses() {
        return courseRepository.findAll().stream().map(this::toCourseGridDto).toList();
    }

    @Transactional(readOnly = true)
    public StudentCreateDto findFormById(Long id) {
        return toCreateDto(findEntityById(id));
    }

    private Student findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private void apply(StudentCreateDto dto, Student student) {
        student.setStudentName(dto.getStudentName());
        student.setAge(dto.getAge());
        student.setGpa(dto.getGpa());
        student.setGroupName(dto.getGroupName());
        student.setUser(findUser(dto.getUserId()));
        List<Long> courseIds = dto.getCourseIds();
        student.setCourses(courseIds == null ? new HashSet<>() : new HashSet<>(courseRepository.findAllById(courseIds)));
    }

    private StudentCreateDto toCreateDto(Student student) {
        StudentCreateDto dto = new StudentCreateDto();
        dto.setId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setAge(student.getAge());
        dto.setGpa(student.getGpa());
        dto.setGroupName(student.getGroupName());
        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
        }
        if (student.getCourses() != null) {
            dto.setCourseIds(student.getCourses().stream().map(Course::getId).toList());
        }
        return dto;
    }

    private StudentDto toDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setAge(student.getAge());
        dto.setGpa(student.getGpa());
        dto.setGroupName(student.getGroupName());
        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
            dto.setUserName(student.getUser().getUserName());
        }
        if (student.getCourses() != null) {
            dto.setCourseIds(student.getCourses().stream().map(Course::getId).toList());
            dto.setCourseNames(student.getCourses().stream().map(Course::getCourseName).toList());
        }
        return dto;
    }

    private UserGridDto toUserGridDto(User user) {
        if (user == null) {
            return null;
        }
        UserGridDto dto = new UserGridDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private CourseGridDto toCourseGridDto(Course course) {
        if (course == null) {
            return null;
        }
        CourseGridDto dto = new CourseGridDto();
        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setCredits(course.getCredits());
        dto.setMaxStudents(course.getMaxStudents());
        return dto;
    }
}
