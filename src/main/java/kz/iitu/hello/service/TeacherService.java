package kz.iitu.hello.service;

import kz.iitu.hello.domain.entity.Course;
import kz.iitu.hello.domain.entity.Teacher;
import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.repository.CoursesRepository;
import kz.iitu.hello.domain.repository.TeachersRepository;
import kz.iitu.hello.domain.repository.UsersRepository;
import kz.iitu.hello.exception.BusinessException;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.web.converter.TeacherConverter;
import kz.iitu.hello.web.dto.form.TeacherFormDto;
import kz.iitu.hello.web.dto.grid.CourseGridDto;
import kz.iitu.hello.web.dto.grid.UserGridDto;
import kz.iitu.hello.web.dto.view.TeacherViewDto;
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
    private final TeacherConverter teacherConverter;

    public List<TeacherViewDto> findAllView() {
        return teachersRepository.findAll().stream().map(teacherConverter::toViewDto).toList();
    }

    public List<UserGridDto> findAllUsers() {
        return usersRepository.findAll().stream().map(teacherConverter::toUserGridDto).toList();
    }

    public List<CourseGridDto> findAllCourses() {
        return coursesRepository.findAll().stream().map(teacherConverter::toCourseGridDto).toList();
    }

    public TeacherFormDto getForm(Long id) {
        return id == null ? new TeacherFormDto() : teacherConverter.toFormDto(findById(id));
    }

    public void create(TeacherFormDto form) {
        Teacher teacher = new Teacher();
        applyForm(form, teacher);
        teachersRepository.save(teacher);
    }

    public void update(Long id, TeacherFormDto form) {
        Teacher teacher = findById(id);
        applyForm(form, teacher);
        teachersRepository.save(teacher);
    }

    public void delete(Long id) {
        Teacher teacher = findById(id);
        if (teacher.getCourses() != null && !teacher.getCourses().isEmpty()) {
            throw new BusinessException("Cannot delete teacher with assigned courses");
        }
        teachersRepository.delete(teacher);
    }

    public Teacher findById(Long id) {
        return teachersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + id));
    }

    private void applyForm(TeacherFormDto form, Teacher teacher) {
        User user = usersRepository.findById(form.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + form.getUserId()));
        List<Long> courseIds = form.getCourseIds();
        List<Course> courses = courseIds == null ? new ArrayList<>() : new ArrayList<>(coursesRepository.findAllById(courseIds));
        teacherConverter.applyFormToEntity(form, teacher, user, courses);
    }
}
