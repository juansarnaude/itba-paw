package ar.edu.itba.paw.exceptions;

public class UserVerifiedException  extends RuntimeException{
    public UserVerifiedException() {
    }

    public UserVerifiedException(String message) {
        super(message);
    }

    public UserVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserVerifiedException(Throwable cause) {
        super(cause);
    }

    public UserVerifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
