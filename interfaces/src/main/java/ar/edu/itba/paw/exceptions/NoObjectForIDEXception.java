package ar.edu.itba.paw.exceptions;

public class NoObjectForIDEXception extends RuntimeException{
    public NoObjectForIDEXception() {
    }

    public NoObjectForIDEXception(String message) {
        super(message);
    }

    public NoObjectForIDEXception(String message, Throwable cause) {
        super(message, cause);
    }

    public NoObjectForIDEXception(Throwable cause) {
        super(cause);
    }

    public NoObjectForIDEXception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
