package ar.edu.itba.paw.exceptions;

public class MoovieListNotFoundException extends RuntimeException{
    public MoovieListNotFoundException() {
    }

    public MoovieListNotFoundException(String message) {
        super(message);
    }

    public MoovieListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoovieListNotFoundException(Throwable cause) {
        super(cause);
    }

    public MoovieListNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
