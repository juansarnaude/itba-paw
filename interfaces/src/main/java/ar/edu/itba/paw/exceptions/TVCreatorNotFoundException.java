package ar.edu.itba.paw.exceptions;

public class TVCreatorNotFoundException extends RuntimeException{
    public TVCreatorNotFoundException() {
    }

    public TVCreatorNotFoundException(String message) {
        super(message);
    }

    public TVCreatorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TVCreatorNotFoundException(Throwable cause) {
        super(cause);
    }

    public TVCreatorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
