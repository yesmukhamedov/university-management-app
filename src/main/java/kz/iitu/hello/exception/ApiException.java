package kz.iitu.hello.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
