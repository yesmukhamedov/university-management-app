package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.web.dto.form.StudentFormDto;
import kz.iitu.hello.web.dto.view.StudentViewDto;
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
