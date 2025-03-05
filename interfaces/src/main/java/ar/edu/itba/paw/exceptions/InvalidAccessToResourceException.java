package ar.edu.itba.paw.exceptions;

public class InvalidAccessToResourceException extends RuntimeException{
    public InvalidAccessToResourceException() {
    }

    public InvalidAccessToResourceException(String message) {
        super(message);
    }

    public InvalidAccessToResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessToResourceException(Throwable cause) {
        super(cause);
    }

    public InvalidAccessToResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
