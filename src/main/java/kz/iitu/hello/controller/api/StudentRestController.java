package kz.iitu.hello.controller.api;

import jakarta.validation.Valid;
import kz.iitu.hello.dto.StudentCreateDto;
import kz.iitu.hello.dto.StudentDto;
import kz.iitu.hello.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public StudentDto findById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @PostMapping
    public StudentDto create(@Valid @RequestBody StudentCreateDto createDto) {
        return studentService.create(createDto);
    }

    @PutMapping("/{id}")
    public StudentDto update(@PathVariable Long id, @Valid @RequestBody StudentCreateDto createDto) {
        return studentService.update(id, createDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
