package ar.edu.itba.paw.exceptions;

public class DirectorNotFoundException extends RuntimeException{
    public DirectorNotFoundException() {
    }

    public DirectorNotFoundException(String message) {
        super(message);
    }

    public DirectorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectorNotFoundException(Throwable cause) {
        super(cause);
    }

    public DirectorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
