package kz.iitu.hello;

import kz.iitu.hello.entity.Student;
import kz.iitu.hello.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesRepository coursesRepository;
    private final TeachersRepository teachersRepository;
    private final StudentRepository studentsRepository;

    public CoursesController(CoursesRepository coursesRepository,
                             TeachersRepository teachersRepository,
                             StudentRepository studentsRepository) {
        this.coursesRepository = coursesRepository;
        this.teachersRepository = teachersRepository;
        this.studentsRepository = studentsRepository;
    }

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        CourseFormDto form = id != null ? toFormDto(findCourse(id)) : new CourseFormDto();

        model.addAttribute("editMode", id != null);
        model.addAttribute("form", form);
        model.addAttribute("courses", coursesRepository.findAll().stream().map(this::toViewDto).toList());
        model.addAttribute("teachers", teachersRepository.findAll().stream().map(this::toTeacherGridDto).toList());
        model.addAttribute("students", studentsRepository.findAll().stream().map(this::toStudentGridDto).toList());

        return "courses";
    }

    @PostMapping
    public String create(@ModelAttribute("form") CourseFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        validateCourseForm(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return renderFormWithErrors(model, form, false);
        }

        Course course = new Course();
        applyFormToEntity(form, course);
        coursesRepository.save(course);
        return "redirect:/courses";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") CourseFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        Course course = findCourse(id);
        validateCourseForm(form, bindingResult);
        if (bindingResult.hasErrors()) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }

        applyFormToEntity(form, course);
        coursesRepository.save(course);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Course course = findCourse(id);

        if (course.getStudents() != null) {
            for (Student student : course.getStudents()) {
                if (student.getCourses() != null) {
                    student.getCourses().remove(course);
                }
            }
            course.getStudents().clear();
        }
        coursesRepository.deleteById(id);
        return "redirect:/courses";
    }

    private String renderFormWithErrors(Model model, CourseFormDto form, boolean editMode) {
        model.addAttribute("editMode", editMode);
        model.addAttribute("form", form);
        model.addAttribute("courses", coursesRepository.findAll().stream().map(this::toViewDto).toList());
        model.addAttribute("teachers", teachersRepository.findAll().stream().map(this::toTeacherGridDto).toList());
        model.addAttribute("students", studentsRepository.findAll().stream().map(this::toStudentGridDto).toList());
        return "courses";
    }

    private void validateCourseForm(CourseFormDto form, BindingResult bindingResult) {
        if (form.getCourseName() == null || form.getCourseName().isBlank()) {
            bindingResult.rejectValue("courseName", "courseName.blank", "Course name is required");
        }
        if (form.getCredits() == null) {
            bindingResult.rejectValue("credits", "credits.blank", "Credits is required");
        } else if (form.getCredits() < 1 || form.getCredits() > 10) {
            bindingResult.rejectValue("credits", "credits.invalid", "Credits must be between 1 and 10");
        }
        if (form.getMaxStudents() == null) {
            bindingResult.rejectValue("maxStudents", "maxStudents.blank", "Max students is required");
        } else if (form.getMaxStudents() < 1) {
            bindingResult.rejectValue("maxStudents", "maxStudents.invalid", "Max students must be positive");
        }
        if (form.getTeacherId() == null) {
            bindingResult.rejectValue("teacherId", "teacherId.blank", "Teacher is required");
        }
    }

    private Course findCourse(Long id) {
        return coursesRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
    }

    private Teacher findTeacher(Long id) {
        return teachersRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));
    }

    private void applyFormToEntity(CourseFormDto form, Course course) {
        course.setCourseName(form.getCourseName());
        course.setCredits(form.getCredits());
        course.setMaxStudents(form.getMaxStudents());
        course.setTeacher(findTeacher(form.getTeacherId()));

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
        course.setStudents(students);
        for (Student student : students) {
            Set<Course> studentCourses = student.getCourses() == null ? new HashSet<>() : student.getCourses();
            studentCourses.add(course);
            student.setCourses(studentCourses);
        }
    }

    private CourseFormDto toFormDto(Course course) {
        CourseFormDto dto = new CourseFormDto();
        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setCredits(course.getCredits());
        dto.setMaxStudents(course.getMaxStudents());
        if (course.getTeacher() != null) {
            dto.setTeacherId(course.getTeacher().getId());
        }
        if (course.getStudents() != null) {
            dto.setStudentIds(course.getStudents().stream().map(Student::getId).toList());
        }
        return dto;
    }

    private CourseViewDto toViewDto(Course course) {
        CourseViewDto dto = new CourseViewDto();
        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setCredits(course.getCredits());
        dto.setMaxStudents(course.getMaxStudents());
        if (course.getTeacher() != null) {
            dto.setTeacher(toTeacherGridDto(course.getTeacher()));
        }

        List<StudentGridDto> students = new ArrayList<>();
        if (course.getStudents() != null) {
            for (Student student : course.getStudents()) {
                students.add(toStudentGridDto(student));
            }
        }
        dto.setStudents(students);
        return dto;
    }

    private TeacherGridDto toTeacherGridDto(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        TeacherGridDto dto = new TeacherGridDto();
        dto.setId(teacher.getId());
        dto.setTeacherName(teacher.getTeacherName());
        dto.setDepartment(teacher.getDepartment());
        dto.setExperienceYears(teacher.getExperienceYears());
        return dto;
    }

    private StudentGridDto toStudentGridDto(Student student) {
        if (student == null) {
            return null;
        }
        StudentGridDto dto = new StudentGridDto();
        dto.setId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setAge(student.getAge());
        dto.setGroupName(student.getGroupName());
        dto.setGpa(student.getGpa());
        return dto;
    }
}
