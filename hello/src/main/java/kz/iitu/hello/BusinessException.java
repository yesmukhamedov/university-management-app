package kz.iitu.hello;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
