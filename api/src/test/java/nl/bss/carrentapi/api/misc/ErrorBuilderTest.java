package nl.bss.carrentapi.api.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Map;

class ErrorBuilderTest {
    private Clock clock = Clock.system(ZoneId.of("Europe/Amsterdam"));

    @Test
    void validateErrorBuilderReturnsProperStatusCode() {
        ErrorBuilder builder = new ErrorBuilder(HttpStatus.BAD_REQUEST, clock);
        ResponseEntity result = builder.getResultResponseEntity();
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void validateErrorBuilderReturnsProperError() {
        String error = "MISTAKE HAPPENED";
        ErrorBuilder builder = new ErrorBuilder(error, HttpStatus.BAD_REQUEST, clock);
        ResponseEntity<Map<String, Object>> result = builder.getResultResponseEntity();
        assertEquals(error, result.getBody().get("error"));
    }
}
