package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.exceptions.NotAllowedException;
import nl.bss.carrentapi.api.misc.ErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;

@RestControllerAdvice
public class NotAllowedExceptionHandler {
    private final Clock clock;

    public NotAllowedExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(NotAllowedException.class)
    @ResponseBody
    public ResponseEntity NotAllowedExceptionHandler(NotAllowedException ex) {
        String reason = ex.getMessage();
        if (reason == null) {
            reason = "You are not allowed to do that.";
        }

        ErrorBuilder error = new ErrorBuilder(reason, HttpStatus.FORBIDDEN, clock);
        return error.getResultResponseEntity();
    }
}
