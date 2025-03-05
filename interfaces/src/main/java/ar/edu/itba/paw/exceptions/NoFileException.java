package ar.edu.itba.paw.exceptions;

public class NoFileException extends RuntimeException{
    public NoFileException() {
    }

    public NoFileException(String message) {
        super(message);
    }

    public NoFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFileException(Throwable cause) {
        super(cause);
    }

    public NoFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
