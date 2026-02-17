package kz.iitu.hello;
import org.springframework.http.HttpStatus;

public class CourseLimitExceededException extends ApiException {

    public CourseLimitExceededException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
