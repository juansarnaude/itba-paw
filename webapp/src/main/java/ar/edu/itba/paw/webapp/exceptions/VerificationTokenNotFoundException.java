package ar.edu.itba.paw.webapp.exceptions;

public class VerificationTokenNotFoundException extends RuntimeException{
    public VerificationTokenNotFoundException() {
    }

    public VerificationTokenNotFoundException(String message) {
        super(message);
    }

    public VerificationTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    public VerificationTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
