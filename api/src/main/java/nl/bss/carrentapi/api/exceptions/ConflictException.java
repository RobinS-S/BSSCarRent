package nl.bss.carrentapi.api.exceptions;

/**
 * Exception that occurs when you cause a conflicted situation.
 */
public class ConflictException extends RuntimeException {
    public ConflictException() {
        super();
    }

    public ConflictException(String reason) {
        super(reason);
    }
}
