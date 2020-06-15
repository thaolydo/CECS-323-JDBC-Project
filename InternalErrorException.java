package project.jdbc;

public class InternalErrorException extends RuntimeException {

    public InternalErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InternalErrorException(String msg) {
        super(msg);
    }
    
}