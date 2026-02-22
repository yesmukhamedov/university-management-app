package kz.iitu.hello.exception;

import jakarta.servlet.http.HttpServletRequest;
import kz.iitu.hello.web.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, StudentNotFoundException.class, TeacherNotFoundException.class, CourseNotFoundException.class, ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(404).body(buildErrorResponse(404, "Not Found", ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler({DuplicateUserException.class, DuplicateResourceException.class, BusinessException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(409).body(buildErrorResponse(409, "Conflict", ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler({InvalidRoleException.class, CourseLimitExceededException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(buildErrorResponse(400, "Bad Request", ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(buildErrorResponse(400, "Bad Request", "Validation failed", request.getRequestURI(), validationErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.internalServerError().body(buildErrorResponse(500, "Internal Server Error", ex.getMessage(), request.getRequestURI(), null));
    }

    private ErrorResponse buildErrorResponse(int status,
                                             String error,
                                             String message,
                                             String path,
                                             Map<String, String> validationErrors) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, validationErrors);
    }
}
