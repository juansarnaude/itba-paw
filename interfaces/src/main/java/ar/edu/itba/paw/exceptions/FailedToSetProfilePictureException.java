package ar.edu.itba.paw.exceptions;

public class FailedToSetProfilePictureException extends RuntimeException{
    public FailedToSetProfilePictureException() {
    }

    public FailedToSetProfilePictureException(String message) {
        super(message);
    }

    public FailedToSetProfilePictureException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToSetProfilePictureException(Throwable cause) {
        super(cause);
    }

    public FailedToSetProfilePictureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
