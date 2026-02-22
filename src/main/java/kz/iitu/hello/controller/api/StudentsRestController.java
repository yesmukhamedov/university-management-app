package kz.iitu.hello.controller.api;

import kz.iitu.hello.dto.form.StudentFormDto;
import kz.iitu.hello.dto.view.StudentViewDto;
import kz.iitu.hello.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentsRestController {
    private final StudentService studentService;

    public StudentsRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentViewDto> read() { return studentService.findAllView(); }

    @PostMapping
    public void create(@RequestBody StudentFormDto form) { studentService.create(form); }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody StudentFormDto form) { studentService.update(id, form); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { studentService.delete(id); }
}
