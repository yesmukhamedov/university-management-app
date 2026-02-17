package kz.iitu.hello;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
