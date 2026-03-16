package kz.iitu.hello.web.controller.api;

import kz.iitu.hello.service.TeacherService;
import kz.iitu.hello.web.dto.form.TeacherFormDto;
import kz.iitu.hello.web.dto.search.TeacherSearchForm;
import kz.iitu.hello.web.dto.view.TeacherViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private final TeacherService teacherService;

    @GetMapping
    public Page<TeacherViewDto> read(TeacherSearchForm form,
                                     @PageableDefault(size = 10) Pageable pageable) {
        return teacherService.search(form, pageable);
    }

    @PostMapping
    public void create(@RequestBody TeacherFormDto form) {
        teacherService.create(form);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody TeacherFormDto form) {
        teacherService.update(id, form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
