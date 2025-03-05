package ar.edu.itba.paw.exceptions;

public class UnableToCreateUserException extends RuntimeException{
    public UnableToCreateUserException() {
    }

    public UnableToCreateUserException(String message) {
        super(message);
    }

    public UnableToCreateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToCreateUserException(Throwable cause) {
        super(cause);
    }

    public UnableToCreateUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
