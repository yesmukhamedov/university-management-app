package kz.iitu.hello.domain.repository;

import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u
            FROM User u
            WHERE
            LOWER(u.userName) LIKE LOWER(CONCAT('%', :username, '%'))
            AND LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))
            AND (:role IS NULL OR u.role = :role)
            """)
    Page<User> search(@Param("username") String username,
                      @Param("email") String email,
                      @Param("role") UserRole role,
                      Pageable pageable);
}
