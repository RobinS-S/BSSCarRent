package nl.bss.carrentapi.api.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String reason) {
        super(reason);
    }
}
