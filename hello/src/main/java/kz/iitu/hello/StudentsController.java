package kz.iitu.hello;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private final StudentsRepository studentRepository;
    private final UsersRepository userRepository;
    private final CoursesRepository courseRepository;

    public StudentsController(StudentsRepository studentRepository,
                              UsersRepository userRepository,
                              CoursesRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        StudentFormDto form = id == null ? new StudentFormDto() : toFormDto(findStudent(id));
        model.addAttribute("form", form);
        model.addAttribute("editMode", id != null);
        model.addAttribute("students", studentRepository.findAll().stream().map(this::toViewDto).toList());
        model.addAttribute("users", userRepository.findAll().stream().map(this::toUserGridDto).toList());
        model.addAttribute("courses", courseRepository.findAll().stream().map(this::toCourseGridDto).toList());
        return "students";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") StudentFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return renderFormWithErrors(model, form, false);
        }

        Student student = new Student();
        applyFormToEntity(form, student);
        studentRepository.save(student);
        return "redirect:/students";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") StudentFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        Student student = findStudent(id);
        if (bindingResult.hasErrors()) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }

        applyFormToEntity(form, student);
        studentRepository.save(student);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Student student = findStudent(id);
        if (student.getCourses() != null) {
            for (Course course : student.getCourses()) {
                if (course.getStudents() != null) {
                    course.getStudents().remove(student);
                }
            }
            student.getCourses().clear();
        }
        studentRepository.delete(student);
        return "redirect:/students";
    }

    private String renderFormWithErrors(Model model, StudentFormDto form, boolean editMode) {
        model.addAttribute("form", form);
        model.addAttribute("editMode", editMode);
        model.addAttribute("students", studentRepository.findAll().stream().map(this::toViewDto).toList());
        model.addAttribute("users", userRepository.findAll().stream().map(this::toUserGridDto).toList());
        model.addAttribute("courses", courseRepository.findAll().stream().map(this::toCourseGridDto).toList());
        return "students";
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
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

    private StudentViewDto toViewDto(Student student) {
        StudentViewDto dto = new StudentViewDto();
        dto.setId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setAge(student.getAge());
        dto.setGpa(student.getGpa());
        dto.setGroupName(student.getGroupName());
        if (student.getUser() != null) {
            dto.setUser(toUserGridDto(student.getUser()));
        }

        List<CourseGridDto> courseDtos = new ArrayList<>();
        if (student.getCourses() != null) {
            for (Course course : student.getCourses()) {
                courseDtos.add(toCourseGridDto(course));
            }
        }
        dto.setCourses(courseDtos);
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
