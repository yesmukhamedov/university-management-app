package kz.iitu.hello.web.controller.mvc;

import kz.iitu.hello.service.CourseService;
import kz.iitu.hello.web.dto.search.CourseSearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String list(CourseSearchForm form,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {
        model.addAttribute("page", courseService.search(form, pageable));
        model.addAttribute("searchForm", form);
        model.addAttribute("teachers", courseService.findAllTeachers());
        return "courses/list";
    }
}
