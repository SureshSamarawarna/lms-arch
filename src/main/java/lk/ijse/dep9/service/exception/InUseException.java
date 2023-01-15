package lk.ijse.dep9.service.exception;

public class InUseException extends RuntimeException{

    public InUseException(String message) {
        super(message);
    }

    public InUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
