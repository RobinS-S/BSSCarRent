package nl.bss.carrentapi.api.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super();
    }

    public ConflictException(String reason) {
        super(reason);
    }
}
