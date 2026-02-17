package kz.iitu.hello;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    public String read(@RequestParam(name = "id", required = false) Long id, Model model) {
        TeacherFormDto form = id != null ? toFormDto(findTeacher(id)) : new TeacherFormDto();
        model.addAttribute("editMode", id != null);
        model.addAttribute("form", form);
        model.addAttribute("teachers", teachersRepository.findAll().stream().map(this::toViewDto).toList());
        model.addAttribute("users", usersRepository.findAll().stream().map(this::toUserGridDto).toList());
        model.addAttribute("courses", courseRepository.findAll().stream().map(this::toCourseGridDto).toList());
        model.addAttribute("departments", Department.values());
        return "teachers";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TeacherFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return renderFormWithErrors(model, form, false);
        }

        Teacher teacher = new Teacher();
        applyFormToEntity(form, teacher);
        teachersRepository.save(teacher);
        return "redirect:/teachers";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") TeacherFormDto form,
                         BindingResult bindingResult,
                         Model model) {
        Teacher teacher = findTeacher(id);
        if (bindingResult.hasErrors()) {
            form.setId(id);
            return renderFormWithErrors(model, form, true);
        }

        applyFormToEntity(form, teacher);
        teachersRepository.save(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Teacher teacher = findTeacher(id);
        if (teacher.getCourses() != null && !teacher.getCourses().isEmpty()) {
            throw new BusinessException("Cannot delete teacher with assigned courses");
        }
        teachersRepository.delete(teacher);
        return "redirect:/teachers";
    }

    private String renderFormWithErrors(Model model, TeacherFormDto form, boolean editMode) {
        model.addAttribute("editMode", editMode);
        model.addAttribute("form", form);
        model.addAttribute("teachers", teachersRepository.findAll().stream().map(this::toViewDto).toList());
        model.addAttribute("users", usersRepository.findAll().stream().map(this::toUserGridDto).toList());
        model.addAttribute("courses", courseRepository.findAll().stream().map(this::toCourseGridDto).toList());
        model.addAttribute("departments", Department.values());
        return "teachers";
    }

    private Teacher findTeacher(Long id) {
        return teachersRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));
    }

    private User findUser(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private void applyFormToEntity(TeacherFormDto form, Teacher teacher) {
        teacher.setTeacherName(form.getTeacherName());
        teacher.setDepartment(form.getDepartment());
        teacher.setExperienceYears(form.getExperienceYears());
        teacher.setUser(findUser(form.getUserId()));

        List<Long> courseIds = form.getCourseIds();
        List<Course> courses = courseIds == null ? new ArrayList<>() : new ArrayList<>(courseRepository.findAllById(courseIds));
        teacher.setCourses(courses);
    }

    private TeacherFormDto toFormDto(Teacher teacher) {
        TeacherFormDto dto = new TeacherFormDto();
        dto.setId(teacher.getId());
        dto.setTeacherName(teacher.getTeacherName());
        dto.setDepartment(teacher.getDepartment());
        dto.setExperienceYears(teacher.getExperienceYears());
        dto.setUserId(teacher.getUser().getId());
        if (teacher.getCourses() != null) {
            dto.setCourseIds(teacher.getCourses().stream().map(Course::getId).toList());
        }
        return dto;
    }

    private TeacherViewDto toViewDto(Teacher teacher) {
        TeacherViewDto dto = new TeacherViewDto();
        dto.setId(teacher.getId());
        dto.setTeacherName(teacher.getTeacherName());
        dto.setDepartment(teacher.getDepartment());
        dto.setExperienceYears(teacher.getExperienceYears());
        dto.setUser(toUserGridDto(teacher.getUser()));

        List<CourseGridDto> courses = new ArrayList<>();
        if (teacher.getCourses() != null) {
            for (Course course : teacher.getCourses()) {
                courses.add(toCourseGridDto(course));
            }
        }
        dto.setCourses(courses);
        return dto;
    }

    private UserGridDto toUserGridDto(User user) {
        UserGridDto dto = new UserGridDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private CourseGridDto toCourseGridDto(Course course) {
        CourseGridDto dto = new CourseGridDto();
        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setCredits(course.getCredits());
        dto.setMaxStudents(course.getMaxStudents());
        return dto;
    }
}
