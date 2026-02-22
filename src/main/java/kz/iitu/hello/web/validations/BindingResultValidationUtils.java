package kz.iitu.hello.web.validations;

import org.springframework.validation.BindingResult;

public final class BindingResultValidationUtils {

    private BindingResultValidationUtils() {
    }

    public static void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }
    }

    public static boolean hasErrors(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }
}
