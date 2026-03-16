package kz.iitu.hello.web.controller.mvc;

import kz.iitu.hello.service.StudentService;
import kz.iitu.hello.web.dto.search.StudentSearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String list(StudentSearchForm form,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {
        model.addAttribute("page", studentService.search(form, pageable));
        model.addAttribute("searchForm", form);
        return "students/list";
    }
}
