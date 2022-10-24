package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.exceptions.UnauthenticatedException;
import nl.bss.carrentapi.api.misc.ErrorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UnauthenticatedExceptionHandler {
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public ResponseEntity UnauthorizedExceptionHandler(UnauthenticatedException ex) {
        String reason = ex.getMessage();
        if (reason == null) {
            reason = "You are not authenticated. Use Basic authentication with your e-mail address.";
        }

        ErrorBuilder error = new ErrorBuilder(reason, HttpStatus.UNAUTHORIZED);
        error.getHttpHeaders().set("WWW-Authenticate", "Basic"); // This is the convention for indicating Authorization header schema
        return error.getResultResponseEntity();
    }
}
