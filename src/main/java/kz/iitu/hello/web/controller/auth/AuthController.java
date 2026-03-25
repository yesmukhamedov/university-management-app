package kz.iitu.hello.web.controller.auth;

import kz.iitu.hello.domain.entity.User;
import kz.iitu.hello.domain.enums.UserRole;
import kz.iitu.hello.domain.repository.UsersRepository;
import kz.iitu.hello.security.JwtUtil;
import kz.iitu.hello.web.dto.auth.AuthResponse;
import kz.iitu.hello.web.dto.auth.LoginRequest;
import kz.iitu.hello.web.dto.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        // Delegates username/password verification to Spring Security.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getUsername());
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        if (usersRepository.findByUserName(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = new User();
        user.setUserName(request.getUsername());
        user.setEmail(request.getEmail());
        // Store only hashed passwords.
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? UserRole.STUDENT : request.getRole());

        usersRepository.save(user);

        String token = jwtUtil.generateToken(user.getUserName());
        return new AuthResponse(token);
    }
}
