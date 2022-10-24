package nl.bss.carrentapi.api.exceptions;

/**
 * Exception that occurs when you are not logged in.
 */
public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super();
    }

    public UnauthenticatedException(String reason) {
        super(reason);
    }
}
