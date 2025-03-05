package ar.edu.itba.paw.exceptions;

public class UnableToInsertIntoDatabase extends RuntimeException{
    public UnableToInsertIntoDatabase() {
    }

    public UnableToInsertIntoDatabase(String message) {
        super(message);
    }

    public UnableToInsertIntoDatabase(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToInsertIntoDatabase(Throwable cause) {
        super(cause);
    }

    public UnableToInsertIntoDatabase(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
