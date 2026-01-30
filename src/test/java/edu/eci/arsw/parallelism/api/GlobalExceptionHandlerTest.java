package edu.eci.arsw.parallelism.api;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle ConstraintViolationException with 400 status")
    void shouldHandleConstraintViolation() {
        ConstraintViolationException ex = new ConstraintViolationException("start must be >= 0", Set.of());
        
        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertTrue(response.getBody().get("message").toString().contains("Validation error"));
    }

    @Test
    @DisplayName("Should handle MissingServletRequestParameterException with 400 status")
    void shouldHandleMissingParam() {
        MissingServletRequestParameterException ex = 
            new MissingServletRequestParameterException("count", "int");
        
        ResponseEntity<Map<String, Object>> response = handler.handleMissingParam(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertTrue(response.getBody().get("message").toString().contains("Missing parameter: count"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException with 400 status")
    void shouldHandleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = 
            new MethodArgumentTypeMismatchException("abc", Integer.class, "start", null, null);
        
        ResponseEntity<Map<String, Object>> response = handler.handleTypeMismatch(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertTrue(response.getBody().get("message").toString().contains("Invalid parameter type: start"));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with 400 status")
    void shouldHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid value provided");
        
        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgument(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Invalid value provided", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Should handle generic Exception with 500 status")
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Something went wrong");
        
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("Internal server error", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Response should contain timestamp")
    void responseShouldContainTimestamp() {
        Exception ex = new RuntimeException("Test");
        
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
        
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("timestamp"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    @DisplayName("Should handle HandlerMethodValidationException with 400 status")
    void shouldHandleMethodValidation() {
        HandlerMethodValidationException ex = mock(HandlerMethodValidationException.class);
        when(ex.getMessage()).thenReturn("Validation failed for parameter");
        when(ex.getAllErrors()).thenReturn(List.of());
        
        ResponseEntity<Map<String, Object>> response = handler.handleMethodValidation(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertTrue(response.getBody().get("message").toString().contains("Validation failed"));
    }
}
