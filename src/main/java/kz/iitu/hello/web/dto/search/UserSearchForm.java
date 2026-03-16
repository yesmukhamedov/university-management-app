package kz.iitu.hello.web.dto.search;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class UserSearchForm {
    private String username;
    private String email;
    private String sortBy;
    private Sort.Direction sortDirection;
}
