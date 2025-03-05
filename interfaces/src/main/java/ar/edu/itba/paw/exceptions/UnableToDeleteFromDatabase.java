package ar.edu.itba.paw.exceptions;

public class UnableToDeleteFromDatabase extends RuntimeException{
    public UnableToDeleteFromDatabase() {
    }

    public UnableToDeleteFromDatabase(String message) {
        super(message);
    }

    public UnableToDeleteFromDatabase(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToDeleteFromDatabase(Throwable cause) {
        super(cause);
    }

    public UnableToDeleteFromDatabase(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
