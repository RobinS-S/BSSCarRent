package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.exceptions.ConflictException;
import nl.bss.carrentapi.api.misc.ErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;

@RestControllerAdvice
public class ConflictExceptionHandler {
    private final Clock clock;

    public ConflictExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    public ResponseEntity ConflictExceptionHandler(ConflictException ex) {
        String reason = ex.getMessage();
        if (reason == null) {
            reason = "This is impossible.";
        }

        ErrorBuilder error = new ErrorBuilder(reason, HttpStatus.CONFLICT, clock);
        return error.getResultResponseEntity();
    }
}
