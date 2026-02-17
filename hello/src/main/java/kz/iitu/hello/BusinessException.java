package kz.iitu.hello;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {
    public BusinessException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
