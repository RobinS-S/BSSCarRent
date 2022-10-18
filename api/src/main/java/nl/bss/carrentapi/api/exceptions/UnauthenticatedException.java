package nl.bss.carrentapi.api.exceptions;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super();
    }

    public UnauthenticatedException(String reason) {
        super(reason);
    }
}
