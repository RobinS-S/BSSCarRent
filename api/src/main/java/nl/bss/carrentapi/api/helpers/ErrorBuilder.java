package nl.bss.carrentapi.api.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorBuilder {
    private Map<String, Object> result = new HashMap<>();
    private HttpStatus status;

    public ErrorBuilder(HttpStatus status) {
        this.status = status;
        result.put("timestamp", LocalDateTime.now());
        result.put("status", status.value());
    }

    public ErrorBuilder(HttpStatus status, String error) {
        this(status);
        result.put("error", error);
    }

    public ErrorBuilder(HttpStatus status, String field, String error) {
        this(status);
        result.put("errors", new HashMap<>() { { put(field, error ); }});
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
        return new ResponseEntity<>(result, status);
    }
}
