package nl.bss.carrentapi.api.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

class ErrorBuilderTest {
    @Test
    void validateErrorBuilderReturnsProperStatusCode() {
        ErrorBuilder builder = new ErrorBuilder(HttpStatus.BAD_REQUEST);
        ResponseEntity result = builder.getResultResponseEntity();
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void validateErrorBuilderReturnsProperError() {
        String error = "MISTAKE HAPPENED";
        ErrorBuilder builder = new ErrorBuilder(error, HttpStatus.BAD_REQUEST);
        ResponseEntity<Map<String, Object>> result = builder.getResultResponseEntity();
        assertEquals(error, result.getBody().get("error"));
    }
}
