package ar.edu.itba.paw.exceptions;

public class UnableToFindUserException extends RuntimeException{
    public UnableToFindUserException() {
    }

    public UnableToFindUserException(String message) {
        super(message);
    }

    public UnableToFindUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToFindUserException(Throwable cause) {
        super(cause);
    }

    public UnableToFindUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
