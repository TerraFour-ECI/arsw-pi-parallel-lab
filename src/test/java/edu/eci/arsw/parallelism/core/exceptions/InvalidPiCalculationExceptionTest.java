package edu.eci.arsw.parallelism.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InvalidPiCalculationException Unit Tests")
class InvalidPiCalculationExceptionTest {

    @Test
    @DisplayName("Should create exception with message, field and rejected value")
    void shouldCreateExceptionWithAllParameters() {
        String message = "Invalid parameter";
        String field = "count";
        Object rejectedValue = -5;

        InvalidPiCalculationException exception =
            new InvalidPiCalculationException(message, field, rejectedValue);

        assertEquals(message, exception.getMessage());
        assertEquals(field, exception.getField());
        assertEquals(rejectedValue, exception.getRejectedValue());
    }

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        String message = "Something went wrong";

        InvalidPiCalculationException exception =
            new InvalidPiCalculationException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getField());
        assertNull(exception.getRejectedValue());
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void shouldBeRuntimeException() {
        InvalidPiCalculationException exception =
            new InvalidPiCalculationException("Test");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Should handle null rejected value")
    void shouldHandleNullRejectedValue() {
        InvalidPiCalculationException exception =
            new InvalidPiCalculationException("Error", "field", null);

        assertEquals("Error", exception.getMessage());
        assertEquals("field", exception.getField());
        assertNull(exception.getRejectedValue());
    }

    @Test
    @DisplayName("Should handle various rejected value types")
    void shouldHandleVariousRejectedValueTypes() {
        InvalidPiCalculationException exceptionWithString =
            new InvalidPiCalculationException("Error", "field", "invalid");
        InvalidPiCalculationException exceptionWithDouble =
            new InvalidPiCalculationException("Error", "field", 3.14);

        assertEquals("invalid", exceptionWithString.getRejectedValue());
        assertEquals(3.14, exceptionWithDouble.getRejectedValue());
    }
}
