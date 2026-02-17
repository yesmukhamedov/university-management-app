package kz.iitu.hello;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFormDto {
    @NotBlank
    @Size(min = 4)
    private String userName;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private UserRole role;
}
