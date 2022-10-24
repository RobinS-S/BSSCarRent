package nl.bss.carrentapi.api.exceptions;

/**
 * Exception that occurs when you try to do give data that isn't right.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String reason) {
        super(reason);
    }
}
