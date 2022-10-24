package nl.bss.carrentapi.api.exceptions;

/**
 * Exception that occurs when something can't be found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String reason) {
        super(reason);
    }
}
