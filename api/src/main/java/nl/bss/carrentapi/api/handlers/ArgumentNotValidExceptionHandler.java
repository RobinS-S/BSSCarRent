package nl.bss.carrentapi.api.handlers;

import nl.bss.carrentapi.api.misc.ErrorBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Clock;

@ControllerAdvice
public class ArgumentNotValidExceptionHandler extends ResponseEntityExceptionHandler {
    private Clock clock;

    @Override
    @ResponseBody
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorBuilder builder = new ErrorBuilder(HttpStatus.BAD_REQUEST, clock);

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            builder.setError(fieldName, errorMessage);
        });

        return builder.getResultResponseEntity();
    }
}
