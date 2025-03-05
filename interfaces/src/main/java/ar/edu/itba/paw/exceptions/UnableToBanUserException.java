package ar.edu.itba.paw.exceptions;

public class UnableToBanUserException extends RuntimeException{

    public UnableToBanUserException() {
    }

    public UnableToBanUserException(String message) {
        super(message);
    }

    public UnableToBanUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToBanUserException(Throwable cause) {
        super(cause);
    }

    public UnableToBanUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
