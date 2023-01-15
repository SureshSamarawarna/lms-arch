package lk.ijse.dep9.service.exception;

public class AlreadyIssuedException extends RuntimeException{

    public AlreadyIssuedException(String message) {
        super(message);
    }

    public AlreadyIssuedException(String message, Throwable cause) {
        super(message, cause);
    }
}
