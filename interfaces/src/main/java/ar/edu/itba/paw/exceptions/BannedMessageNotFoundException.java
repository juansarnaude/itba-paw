package ar.edu.itba.paw.exceptions;

public class BannedMessageNotFoundException extends RuntimeException{
    public BannedMessageNotFoundException() {
    }

    public BannedMessageNotFoundException(String message) {
        super(message);
    }

    public BannedMessageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BannedMessageNotFoundException(Throwable cause) {
        super(cause);
    }

    public BannedMessageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
