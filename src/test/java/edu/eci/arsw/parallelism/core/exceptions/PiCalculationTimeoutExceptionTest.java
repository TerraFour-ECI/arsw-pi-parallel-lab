package edu.eci.arsw.parallelism.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PiCalculationTimeoutException Unit Tests")
class PiCalculationTimeoutExceptionTest {

    @Test
    @DisplayName("Should create exception with message and elapsed millis")
    void shouldCreateExceptionWithParameters() {
        String message = "Calculation timed out";
        long elapsedMillis = 5000L;

        PiCalculationTimeoutException exception =
            new PiCalculationTimeoutException(message, elapsedMillis);

        assertEquals(message, exception.getMessage());
        assertEquals(elapsedMillis, exception.getElapsedMillis());
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void shouldBeRuntimeException() {
        PiCalculationTimeoutException exception =
            new PiCalculationTimeoutException("Timeout", 1000L);

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Should handle zero elapsed millis")
    void shouldHandleZeroElapsedMillis() {
        PiCalculationTimeoutException exception =
            new PiCalculationTimeoutException("Immediate timeout", 0L);

        assertEquals(0L, exception.getElapsedMillis());
    }

    @Test
    @DisplayName("Should handle large elapsed millis")
    void shouldHandleLargeElapsedMillis() {
        long largeValue = Long.MAX_VALUE;

        PiCalculationTimeoutException exception =
            new PiCalculationTimeoutException("Long timeout", largeValue);

        assertEquals(largeValue, exception.getElapsedMillis());
    }

    @Test
    @DisplayName("Should preserve message correctly")
    void shouldPreserveMessage() {
        String detailedMessage = "Pi calculation exceeded timeout of 10 seconds after processing 1000000 digits";

        PiCalculationTimeoutException exception =
            new PiCalculationTimeoutException(detailedMessage, 10000L);

        assertEquals(detailedMessage, exception.getMessage());
    }
}
