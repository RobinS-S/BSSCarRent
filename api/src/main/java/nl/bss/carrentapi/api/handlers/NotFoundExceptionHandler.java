package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.exceptions.NotFoundException;
import nl.bss.carrentapi.api.misc.ErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity NotFoundExceptionHandler(NotFoundException ex) {
        String reason = ex.getMessage();
        if (reason == null) {
            reason = "You are not allowed to do that.";
        }

        ErrorBuilder error = new ErrorBuilder(reason, HttpStatus.FORBIDDEN);
        return error.getResultResponseEntity();
    }
}
