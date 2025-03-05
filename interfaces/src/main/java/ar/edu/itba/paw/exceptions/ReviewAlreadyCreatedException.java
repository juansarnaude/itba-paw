package ar.edu.itba.paw.exceptions;

public class ReviewAlreadyCreatedException extends RuntimeException{
    public ReviewAlreadyCreatedException() {
    }

    public ReviewAlreadyCreatedException(String message) {
        super(message);
    }

    public ReviewAlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewAlreadyCreatedException(Throwable cause) {
        super(cause);
    }

    public ReviewAlreadyCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
