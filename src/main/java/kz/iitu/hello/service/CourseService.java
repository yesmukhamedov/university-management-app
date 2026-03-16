package kz.iitu.hello.service;

import kz.iitu.hello.domain.entity.Course;
import kz.iitu.hello.domain.entity.Student;
import kz.iitu.hello.domain.entity.Teacher;
import kz.iitu.hello.domain.repository.CoursesRepository;
import kz.iitu.hello.domain.repository.StudentsRepository;
import kz.iitu.hello.domain.repository.TeachersRepository;
import kz.iitu.hello.domain.specification.CourseSpecification;
import kz.iitu.hello.exception.EntityNotFoundException;
import kz.iitu.hello.web.converter.CourseConverter;
import kz.iitu.hello.web.dto.form.CourseFormDto;
import kz.iitu.hello.web.dto.grid.StudentGridDto;
import kz.iitu.hello.web.dto.grid.TeacherGridDto;
import kz.iitu.hello.web.dto.search.CourseSearchForm;
import kz.iitu.hello.web.dto.view.CourseViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private static final String DEFAULT_SORT = "courseName";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "courseName", "credits", "maxStudents");

    private final CoursesRepository coursesRepository;
    private final TeachersRepository teachersRepository;
    private final StudentsRepository studentsRepository;
    private final CourseConverter courseConverter;

    public List<CourseViewDto> findAllView() {
        return coursesRepository.findAll().stream().map(courseConverter::toViewDto).toList();
    }

    public List<CourseViewDto> findAllView(String name, Integer minCredits, Integer maxCredits, Long teacherId) {
        CourseSearchForm form = new CourseSearchForm();
        form.setCourseName(name);
        form.setCreditsFrom(minCredits);
        form.setCreditsTo(maxCredits);
        form.setTeacherId(teacherId);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, DEFAULT_SORT));
        return search(form, pageable).getContent();
    }

    public Page<CourseViewDto> search(CourseSearchForm form, Pageable pageable) {
        Sort.Direction direction = form.getSortDirection() == null ? Sort.Direction.ASC : form.getSortDirection();
        String requestedSortBy = form.getSortBy() == null ? DEFAULT_SORT : form.getSortBy();
        String sortBy = ALLOWED_SORT_FIELDS.contains(requestedSortBy) ? requestedSortBy : DEFAULT_SORT;

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));

        return coursesRepository.findAll(CourseSpecification.withFilters(form), sortedPageable)
                .map(courseConverter::toViewDto);
    }

    public List<TeacherGridDto> findAllTeachers() {
        return teachersRepository.findAll().stream().map(courseConverter::toTeacherGridDto).toList();
    }

    public List<StudentGridDto> findAllStudents() {
        return studentsRepository.findAll().stream().map(courseConverter::toStudentGridDto).toList();
    }

    public CourseFormDto getForm(Long id) {
        return id == null ? new CourseFormDto() : courseConverter.toFormDto(findById(id));
    }

    public void create(CourseFormDto form) {
        Course course = new Course();
        applyForm(form, course);
        coursesRepository.save(course);
    }

    public void update(Long id, CourseFormDto form) {
        Course course = findById(id);
        applyForm(form, course);
        coursesRepository.save(course);
    }

    public void delete(Long id) {
        Course course = findById(id);
        if (course.getStudents() != null) {
            for (Student student : course.getStudents()) {
                if (student.getCourses() != null) {
                    student.getCourses().remove(course);
                }
            }
            course.getStudents().clear();
        }
        coursesRepository.deleteById(id);
    }

    public Course findById(Long id) {
        return coursesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }

    private void applyForm(CourseFormDto form, Course course) {
        Teacher teacher = teachersRepository.findById(form.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + form.getTeacherId()));

        if (course.getStudents() != null) {
            for (Student oldStudent : course.getStudents()) {
                if (oldStudent.getCourses() != null) {
                    oldStudent.getCourses().remove(course);
                }
            }
            course.getStudents().clear();
        }

        Set<Student> students = new HashSet<>();
        if (form.getStudentIds() != null) {
            students = new HashSet<>(studentsRepository.findAllById(form.getStudentIds()));
        }

        courseConverter.applyFormToEntity(form, course, teacher, students);

        for (Student student : students) {
            Set<Course> studentCourses = student.getCourses() == null ? new HashSet<>() : student.getCourses();
            studentCourses.add(course);
            student.setCourses(studentCourses);
        }
    }
}
