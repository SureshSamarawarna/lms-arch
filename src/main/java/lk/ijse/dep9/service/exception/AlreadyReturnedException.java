package lk.ijse.dep9.service.exception;

public class AlreadyReturnedException extends RuntimeException{

    public AlreadyReturnedException(String message) {
        super(message);
    }

    public AlreadyReturnedException(String message, Throwable cause) {
        super(message, cause);
    }
}
