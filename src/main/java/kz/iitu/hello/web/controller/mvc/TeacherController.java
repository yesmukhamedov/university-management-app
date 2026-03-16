package kz.iitu.hello.web.controller.mvc;

import kz.iitu.hello.service.TeacherService;
import kz.iitu.hello.web.dto.search.TeacherSearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String list(TeacherSearchForm form,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {
        model.addAttribute("page", teacherService.search(form, pageable));
        model.addAttribute("searchForm", form);
        return "teachers/list";
    }
}
