package nl.bss.carrentapi.api.exceptions;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException() {
        super();
    }

    public NotAllowedException(String reason) {
        super(reason);
    }
}
