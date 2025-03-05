package ar.edu.itba.paw.exceptions;

public class InvalidAuthenticationLevelRequiredToPerformActionException extends RuntimeException{
    public InvalidAuthenticationLevelRequiredToPerformActionException() {
    }

    public InvalidAuthenticationLevelRequiredToPerformActionException(String message) {
        super(message);
    }

    public InvalidAuthenticationLevelRequiredToPerformActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationLevelRequiredToPerformActionException(Throwable cause) {
        super(cause);
    }

    public InvalidAuthenticationLevelRequiredToPerformActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
