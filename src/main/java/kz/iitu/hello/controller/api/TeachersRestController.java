package kz.iitu.hello.controller.api;

import kz.iitu.hello.dto.form.TeacherFormDto;
import kz.iitu.hello.dto.view.TeacherViewDto;
import kz.iitu.hello.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeachersRestController {
    private final TeacherService teacherService;

    public TeachersRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherViewDto> read() { return teacherService.findAllView(); }

    @PostMapping
    public void create(@RequestBody TeacherFormDto form) { teacherService.create(form); }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody TeacherFormDto form) { teacherService.update(id, form); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { teacherService.delete(id); }
}
