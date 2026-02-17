package kz.iitu.hello;

public class CourseLimitExceededException extends RuntimeException {
    public CourseLimitExceededException(String message) {
        super(message);
    }
}
