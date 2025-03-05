package ar.edu.itba.paw.exceptions;

public class UnableToChangeRoleException extends RuntimeException{
    public UnableToChangeRoleException() {
    }

    public UnableToChangeRoleException(String message) {
        super(message);
    }

    public UnableToChangeRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToChangeRoleException(Throwable cause) {
        super(cause);
    }

    public UnableToChangeRoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
