package ar.edu.itba.paw.exceptions;

public class UserNotLoggedException  extends RuntimeException{
    public UserNotLoggedException() {
    }

    public UserNotLoggedException(String message) {
        super(message);
    }

    public UserNotLoggedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLoggedException(Throwable cause) {
        super(cause);
    }

    public UserNotLoggedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
