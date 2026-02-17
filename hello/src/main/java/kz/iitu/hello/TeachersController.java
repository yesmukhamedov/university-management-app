package kz.iitu.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

    private final TeachersRepository teachersRepository;
    private final UsersRepository usersRepository;
    private final CoursesRepository courseRepository;

    public TeachersController(TeachersRepository teachersRepository,
                              UsersRepository usersRepository,
                              CoursesRepository courseRepository) {
        this.teachersRepository = teachersRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
    }

    // READ + FORM
    @GetMapping
    public String read(
            @RequestParam(name = "id", required = false) Long id,
            Model model
    ) {
        Teacher form = (id != null)
                ? teachersRepository.getById(id)
                : new Teacher();

        model.addAttribute("editMode", id != null);
        model.addAttribute("form", form);
        model.addAttribute("teachers", teachersRepository.findAll());
        model.addAttribute("users", usersRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());

        return "teachers";
    }

    // CREATE
    @PostMapping
    public String create(Teacher teacher,
    @RequestParam Long userId,
                         @RequestParam(required = false) List<Long> courseIds) {
        teacher.setUser(usersRepository.getById(userId));

        if (courseIds != null) {
            List<Course> courses = new ArrayList<>(courseRepository.findAllById(courseIds));
            teacher.setCourses(courses);
        }

        teachersRepository.save(teacher);
        return "redirect:/teachers";
    }

    // UPDATE
    @PutMapping("/{id}")
    public String update(@PathVariable(name = "id") Long id,
                         Teacher teacherForm,
                         @RequestParam Long userId,
                         @RequestParam(required = false) List<Long> courseIds) {

        Teacher teacherModel = teachersRepository.getById(id);
        teacherModel.setTeacherName(teacherForm.getTeacherName());
        teacherModel.setUser(usersRepository.getById(userId));

        List<Course> courses = (courseIds == null)
                ? new ArrayList<>()
                : new ArrayList<>(courseRepository.findAllById(courseIds));

        teacherModel.setCourses(courses);
        teachersRepository.save(teacherModel);
        return "redirect:/teachers";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        Teacher teacher = teachersRepository.getById(id);

        for (Course course : teacher.getCourses()) {
            course.getStudents().remove(teacher);
        }
        teacher.getCourses().clear();

        teachersRepository.delete(teacher);
        return "redirect:/teachers";
    }
}
