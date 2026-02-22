package kz.iitu.hello.service;

import kz.iitu.hello.exception.BusinessException;
import kz.iitu.hello.web.dto.form.TeacherFormDto;
import kz.iitu.hello.web.dto.grid.CourseGridDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.web.dto.view.TeacherViewDto;
import kz.iitu.hello.domain.entity.Course;
import kz.iitu.hello.domain.entity.Teacher;
import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.domain.repository.CoursesRepository;
import kz.iitu.hello.domain.repository.TeachersRepository;
import kz.iitu.hello.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {
    private final TeachersRepository teachersRepository;
    private final UsersRepository usersRepository;
    private final CoursesRepository coursesRepository;

    public List<TeacherViewDto> findAllView() { return teachersRepository.findAll().stream().map(this::toViewDto).toList(); }
    public List<UserGridDto> findAllUsers() { return usersRepository.findAll().stream().map(this::toUserGridDto).toList(); }
    public List<CourseGridDto> findAllCourses() { return coursesRepository.findAll().stream().map(this::toCourseGridDto).toList(); }
    public TeacherFormDto getForm(Long id) { return id == null ? new TeacherFormDto() : toFormDto(findById(id)); }

    public void create(TeacherFormDto form) { Teacher t = new Teacher(); applyFormToEntity(form, t); teachersRepository.save(t); }
    public void update(Long id, TeacherFormDto form) { Teacher t = findById(id); applyFormToEntity(form, t); teachersRepository.save(t); }
    public void delete(Long id) {
        Teacher t = findById(id);
        if (t.getCourses() != null && !t.getCourses().isEmpty()) throw new BusinessException("Cannot delete teacher with assigned courses");
        teachersRepository.delete(t);
    }

    public Teacher findById(Long id) {
        return teachersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + id));
    }

    private User findUser(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private void applyFormToEntity(TeacherFormDto form, Teacher teacher) {
        teacher.setTeacherName(form.getTeacherName());
        teacher.setDepartment(form.getDepartment());
        teacher.setExperienceYears(form.getExperienceYears());
        teacher.setUser(findUser(form.getUserId()));
        List<Long> courseIds = form.getCourseIds();
        List<Course> courses = courseIds == null ? new ArrayList<>() : new ArrayList<>(coursesRepository.findAllById(courseIds));
        teacher.setCourses(courses);
    }

    private TeacherFormDto toFormDto(Teacher teacher) {
        TeacherFormDto dto = new TeacherFormDto();
        dto.setId(teacher.getId());
        dto.setTeacherName(teacher.getTeacherName());
        dto.setDepartment(teacher.getDepartment());
        dto.setExperienceYears(teacher.getExperienceYears());
        if (teacher.getUser() != null) dto.setUserId(teacher.getUser().getId());
        if (teacher.getCourses() != null) dto.setCourseIds(teacher.getCourses().stream().map(Course::getId).toList());
        return dto;
    }

    private TeacherViewDto toViewDto(Teacher teacher) {
        TeacherViewDto dto = new TeacherViewDto();
        dto.setId(teacher.getId());
        dto.setTeacherName(teacher.getTeacherName());
        dto.setDepartment(teacher.getDepartment());
        dto.setExperienceYears(teacher.getExperienceYears());
        if (teacher.getUser() != null) dto.setUser(toUserGridDto(teacher.getUser()));
        List<CourseGridDto> courses = new ArrayList<>();
        if (teacher.getCourses() != null) for (Course c : teacher.getCourses()) courses.add(toCourseGridDto(c));
        dto.setCourses(courses);
        return dto;
    }

    private UserGridDto toUserGridDto(User user) {
        if (user == null) return null;
        UserGridDto dto = new UserGridDto();
        dto.setId(user.getId()); dto.setUserName(user.getUserName()); dto.setEmail(user.getEmail()); dto.setRole(user.getRole());
        return dto;
    }

    private CourseGridDto toCourseGridDto(Course course) {
        if (course == null) return null;
        CourseGridDto dto = new CourseGridDto();
        dto.setId(course.getId()); dto.setCourseName(course.getCourseName()); dto.setCredits(course.getCredits()); dto.setMaxStudents(course.getMaxStudents());
        return dto;
    }
}
