package nl.bss.carrentapi.api.exceptions;

/**
 * Exception that occurs when you try to do something that's not allowed.
 */
public class NotAllowedException extends RuntimeException {
    public NotAllowedException() {
        super();
    }

    public NotAllowedException(String reason) {
        super(reason);
    }
}
