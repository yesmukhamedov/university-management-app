package kz.iitu.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesRepository coursesRepository;
    private final TeachersRepository teachersRepository;
    private final StudentsRepository studentsRepository;

    public CoursesController(CoursesRepository coursesRepository,
                             TeachersRepository teachersRepository,
                             StudentsRepository studentsRepository) {
        this.coursesRepository = coursesRepository;
        this.teachersRepository = teachersRepository;
        this.studentsRepository = studentsRepository;
    }

    // READ (list + form)
    @GetMapping
    public String read(
            @RequestParam(name = "id", required = false) Long id,
            Model model) {

        Course form = (id != null)
                ? coursesRepository.getById(id)
                : new Course();

        model.addAttribute("editMode", id != null);
        model.addAttribute("form", form);
        model.addAttribute("courses", coursesRepository.findAll());
        model.addAttribute("teachers", teachersRepository.findAll());
        model.addAttribute("students", studentsRepository.findAll());

        return "courses";
    }

    // CREATE
    @PostMapping
    public String create(Course course, @RequestParam(name="teacherId") Long teacherId, @RequestParam(name="studentIds", required = false) List<Long> studentIds) {
        Teacher teacher = teachersRepository.getById(teacherId);
        course.setTeacher(teacher);

        if (studentIds != null && !studentIds.isEmpty()) {
            Set<Student> students = new HashSet<>(studentsRepository
                    .findAllById(studentIds));

            for (Student student : students) {
                student.getCourses().add(course);
            }
        }

        coursesRepository.save(course);
        return "redirect:/courses";
    }

    // UPDATE
    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            Course courseForm) {

        Course courseModel = coursesRepository.getById(id);

        courseModel.setCourseName(courseForm.getCourseName());
        courseModel.setTeacher(courseForm.getTeacher());

        coursesRepository.save(courseModel);
        return "redirect:/courses";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Course course = coursesRepository.getById(id);

        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
        }

        course.getStudents().clear();
        coursesRepository.deleteById(id);
        return "redirect:/courses";
    }
}
