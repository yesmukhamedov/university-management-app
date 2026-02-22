package kz.iitu.hello.exception;

/**
 * Runtime exception thrown when a requested entity cannot be found.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
