package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.exceptions.ErrorBuilder;
import nl.bss.carrentapi.api.exceptions.UnauthenticatedException;
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
        ErrorBuilder error = new ErrorBuilder("You are not authenticated. Use Basic authentication with your e-mail address.", HttpStatus.UNAUTHORIZED);
        error.getHttpHeaders().set("WWW-Authenticate", "Basic"); // This is the convention for indicating Authorization header schema
        return error.getResultResponseEntity();
    }
}
