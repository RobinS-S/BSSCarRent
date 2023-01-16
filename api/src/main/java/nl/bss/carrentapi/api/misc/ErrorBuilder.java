package nl.bss.carrentapi.api.misc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ErrorBuilder {
    private final Map<String, Object> result = new HashMap<>();
    private final HttpStatus httpStatus;
    private final HttpHeaders httpHeaders;

    public ErrorBuilder(HttpStatus status, Clock clock) {
        httpStatus = status;
        httpHeaders = new HttpHeaders();
        result.put("timestamp", LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public ErrorBuilder(String error, HttpStatus status, Clock clock) {
        this(status, clock);
        result.put("timestamp", LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        result.put("error", error);
    }

    /**
     * Sets 'key' in the 'error' object of the resulting object.
     *
     * @param key
     * @param value
     */
    public void setError(String key, String value) {
        this.result.put(key, value);
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * Returns ResponseEntity that has been built up
     *
     * @return
     */
    public ResponseEntity<Map<String, Object>> getResultResponseEntity() {
        return new ResponseEntity<>(result, httpHeaders, httpStatus);
    }
}
