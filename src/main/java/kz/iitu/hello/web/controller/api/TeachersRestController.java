package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.web.dto.form.TeacherFormDto;
import kz.iitu.hello.web.dto.view.TeacherViewDto;
import kz.iitu.hello.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherViewDto> read() { return teacherService.findAllView(); }

    @PostMapping
    public void create(@RequestBody TeacherFormDto form) { teacherService.create(form); }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody TeacherFormDto form) { teacherService.update(id, form); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { teacherService.delete(id); }
}
