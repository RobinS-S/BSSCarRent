package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.exceptions.BadRequestException;
import nl.bss.carrentapi.api.misc.ErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;

@RestControllerAdvice
public class BadRequestExceptionHandler {
    private final Clock clock;

    public BadRequestExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity BadRequestExceptionHandler(BadRequestException ex) {
        String reason = ex.getMessage();
        if (reason == null) {
            reason = "Bad input.";
        }

        ErrorBuilder error = new ErrorBuilder(reason, HttpStatus.BAD_REQUEST, clock);
        return error.getResultResponseEntity();
    }
}
