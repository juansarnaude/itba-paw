package ar.edu.itba.paw.exceptions;

public class LikingFailedException extends RuntimeException {
    public LikingFailedException() {
    }

    public LikingFailedException(String message) {
        super(message);
    }

    public LikingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LikingFailedException(Throwable cause) {
        super(cause);
    }

    public LikingFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
