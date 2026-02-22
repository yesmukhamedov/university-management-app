package kz.iitu.hello.service;

import kz.iitu.hello.web.dto.form.CourseFormDto;
import kz.iitu.hello.web.dto.grid.StudentGridDto;
import kz.iitu.hello.web.dto.grid.TeacherGridDto;
import kz.iitu.hello.web.dto.view.CourseViewDto;
import kz.iitu.hello.domain.entity.Course;
import kz.iitu.hello.domain.entity.Student;
import kz.iitu.hello.domain.entity.Teacher;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.domain.repository.CoursesRepository;
import kz.iitu.hello.domain.repository.StudentsRepository;
import kz.iitu.hello.domain.repository.TeachersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CourseService {
    private final CoursesRepository coursesRepository;
    private final TeachersRepository teachersRepository;
    private final StudentsRepository studentsRepository;

    public CourseService(CoursesRepository coursesRepository, TeachersRepository teachersRepository, StudentsRepository studentsRepository) {
        this.coursesRepository = coursesRepository;
        this.teachersRepository = teachersRepository;
        this.studentsRepository = studentsRepository;
    }

    public List<CourseViewDto> findAllView() { return coursesRepository.findAll().stream().map(this::toViewDto).toList(); }
    public List<TeacherGridDto> findAllTeachers() { return teachersRepository.findAll().stream().map(this::toTeacherGridDto).toList(); }
    public List<StudentGridDto> findAllStudents() { return studentsRepository.findAll().stream().map(this::toStudentGridDto).toList(); }
    public CourseFormDto getForm(Long id) { return id == null ? new CourseFormDto() : toFormDto(findById(id)); }

    public void create(CourseFormDto form) { Course c = new Course(); applyFormToEntity(form, c); coursesRepository.save(c); }
    public void update(Long id, CourseFormDto form) { Course c = findById(id); applyFormToEntity(form, c); coursesRepository.save(c); }
    public void delete(Long id) {
        Course course = findById(id);
        if (course.getStudents() != null) {
            for (Student student : course.getStudents()) {
                if (student.getCourses() != null) student.getCourses().remove(course);
            }
            course.getStudents().clear();
        }
        coursesRepository.deleteById(id);
    }

    public void validateCourseForm(CourseFormDto form, BindingResult bindingResult) {
        if (form.getCourseName() == null || form.getCourseName().isBlank()) bindingResult.rejectValue("courseName", "courseName.blank", "Course name is required");
        if (form.getCredits() == null) bindingResult.rejectValue("credits", "credits.blank", "Credits is required");
        else if (form.getCredits() < 1 || form.getCredits() > 10) bindingResult.rejectValue("credits", "credits.invalid", "Credits must be between 1 and 10");
        if (form.getMaxStudents() == null) bindingResult.rejectValue("maxStudents", "maxStudents.blank", "Max students is required");
        else if (form.getMaxStudents() < 1) bindingResult.rejectValue("maxStudents", "maxStudents.invalid", "Max students must be positive");
        if (form.getTeacherId() == null) bindingResult.rejectValue("teacherId", "teacherId.blank", "Teacher is required");
    }

    public Course findById(Long id) {
        return coursesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }

    private Teacher findTeacher(Long id) {
        return teachersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + id));
    }

    private void applyFormToEntity(CourseFormDto form, Course course) {
        course.setCourseName(form.getCourseName());
        course.setCredits(form.getCredits());
        course.setMaxStudents(form.getMaxStudents());
        course.setTeacher(findTeacher(form.getTeacherId()));
        if (course.getStudents() != null) {
            for (Student oldStudent : course.getStudents()) {
                if (oldStudent.getCourses() != null) oldStudent.getCourses().remove(course);
            }
            course.getStudents().clear();
        }
        Set<Student> students = new HashSet<>();
        if (form.getStudentIds() != null) students = new HashSet<>(studentsRepository.findAllById(form.getStudentIds()));
        course.setStudents(students);
        for (Student student : students) {
            Set<Course> studentCourses = student.getCourses() == null ? new HashSet<>() : student.getCourses();
            studentCourses.add(course);
            student.setCourses(studentCourses);
        }
    }

    private CourseFormDto toFormDto(Course course) {
        CourseFormDto dto = new CourseFormDto();
        dto.setId(course.getId()); dto.setCourseName(course.getCourseName()); dto.setCredits(course.getCredits()); dto.setMaxStudents(course.getMaxStudents());
        if (course.getTeacher() != null) dto.setTeacherId(course.getTeacher().getId());
        if (course.getStudents() != null) dto.setStudentIds(course.getStudents().stream().map(Student::getId).toList());
        return dto;
    }

    private CourseViewDto toViewDto(Course course) {
        CourseViewDto dto = new CourseViewDto();
        dto.setId(course.getId()); dto.setCourseName(course.getCourseName()); dto.setCredits(course.getCredits()); dto.setMaxStudents(course.getMaxStudents());
        if (course.getTeacher() != null) dto.setTeacher(toTeacherGridDto(course.getTeacher()));
        List<StudentGridDto> students = new ArrayList<>();
        if (course.getStudents() != null) for (Student s : course.getStudents()) students.add(toStudentGridDto(s));
        dto.setStudents(students);
        return dto;
    }

    private TeacherGridDto toTeacherGridDto(Teacher teacher) { if (teacher == null) return null; TeacherGridDto dto = new TeacherGridDto(); dto.setId(teacher.getId()); dto.setTeacherName(teacher.getTeacherName()); dto.setDepartment(teacher.getDepartment()); dto.setExperienceYears(teacher.getExperienceYears()); return dto; }
    private StudentGridDto toStudentGridDto(Student student) { if (student == null) return null; StudentGridDto dto = new StudentGridDto(); dto.setId(student.getId()); dto.setStudentName(student.getStudentName()); dto.setAge(student.getAge()); dto.setGroupName(student.getGroupName()); dto.setGpa(student.getGpa()); return dto; }
}
