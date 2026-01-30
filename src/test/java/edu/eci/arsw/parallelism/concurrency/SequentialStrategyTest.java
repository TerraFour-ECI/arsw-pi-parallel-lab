package edu.eci.arsw.parallelism.concurrency;

import edu.eci.arsw.parallelism.core.PiDigits;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SequentialStrategy Unit Tests")
class SequentialStrategyTest {

    private final SequentialStrategy strategy = new SequentialStrategy();

    @Test
    @DisplayName("Should return correct strategy name")
    void shouldReturnCorrectName() {
        assertEquals("sequential", strategy.name());
    }

    @Test
    @DisplayName("Should calculate Pi digits correctly")
    void shouldCalculatePiDigits() {
        String result = strategy.calculate(0, 10, 1);
        String expected = PiDigits.getDigitsHex(0, 10);
        
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should ignore threads parameter")
    void shouldIgnoreThreadsParameter() {
        String result1 = strategy.calculate(0, 20, 1);
        String result2 = strategy.calculate(0, 20, 4);
        String result3 = strategy.calculate(0, 20, 100);
        
        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    @ParameterizedTest
    @DisplayName("Should produce correct results for various inputs")
    @CsvSource({
        "0, 5",
        "0, 10",
        "5, 10",
        "10, 20",
        "100, 50"
    })
    void shouldProduceCorrectResults(int start, int count) {
        String result = strategy.calculate(start, count, 1);
        String expected = PiDigits.getDigitsHex(start, count);
        
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should return correct length of digits")
    void shouldReturnCorrectLength() {
        int count = 50;
        String result = strategy.calculate(0, count, 1);
        
        assertEquals(count, result.length());
    }

    @Test
    @DisplayName("Should handle large count")
    void shouldHandleLargeCount() {
        String result = strategy.calculate(0, 200, 1);
        String expected = PiDigits.getDigitsHex(0, 200);
        
        assertEquals(expected, result);
    }
}
