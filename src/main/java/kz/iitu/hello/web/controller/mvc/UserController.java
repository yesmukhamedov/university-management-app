package kz.iitu.hello.web.controller.mvc;

import kz.iitu.hello.service.UserService;
import kz.iitu.hello.web.dto.search.UserSearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String list(UserSearchForm form,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {
        model.addAttribute("page", userService.search(form, pageable));
        model.addAttribute("searchForm", form);
        return "users/list";
    }
}
