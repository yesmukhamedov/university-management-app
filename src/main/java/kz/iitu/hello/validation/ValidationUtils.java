package kz.iitu.hello.validation;

import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean hasErrors(BindingResult bindingResult, Model model, String viewName) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("viewName", viewName);
            return true;
        }
        return false;
    }
}
