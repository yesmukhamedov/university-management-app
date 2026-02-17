package kz.iitu.hello;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGridDto {

    private Long id;

    private String userName;

    private UserRole role;
}
