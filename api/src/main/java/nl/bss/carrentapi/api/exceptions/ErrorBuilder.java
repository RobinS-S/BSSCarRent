package nl.bss.carrentapi.api.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorBuilder {
    private final Map<String, Object> result = new HashMap<>();
    private final HttpStatus httpStatus;
    private HttpHeaders httpHeaders;

    public ErrorBuilder(HttpStatus status) {
        httpStatus = status;
        httpHeaders = new HttpHeaders();
    }

    public ErrorBuilder(String error, HttpStatus status) {
        this(status);
        result.put("timestamp", LocalDateTime.now());
        result.put("error", error);
    }

    public void setErrors(Map<String, Object> errors) {
        for (String key : errors.keySet()) {
            this.result.put(key, errors.get(key));
        }
    }

    public void setError(String key, String value) {
        this.result.put(key, value);
    }

    public ResponseEntity getResultResponseEntity() {
        return new ResponseEntity<>(result, httpStatus);
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
