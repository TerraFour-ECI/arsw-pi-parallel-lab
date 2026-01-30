package edu.eci.arsw.parallelism.concurrency;

import edu.eci.arsw.parallelism.core.PiDigits;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ThreadJoinStrategy Unit Tests")
class ThreadJoinStrategyTest {

    private final ThreadJoinStrategy strategy = new ThreadJoinStrategy();

    @Test
    @DisplayName("Should return correct strategy name")
    void shouldReturnCorrectName() {
        assertEquals("threads", strategy.name());
    }

    @Test
    @DisplayName("Should calculate Pi digits with single thread")
    void shouldCalculateWithSingleThread() {
        String result = strategy.calculate(0, 10, 1);
        String expected = PiDigits.getDigitsHex(0, 10);
        
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should calculate Pi digits with multiple threads")
    void shouldCalculateWithMultipleThreads() {
        String result = strategy.calculate(0, 100, 4);
        String expected = PiDigits.getDigitsHex(0, 100);
        
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Should produce same result as sequential for various thread counts")
    @CsvSource({
        "0, 50, 1",
        "0, 50, 2",
        "0, 50, 4",
        "0, 50, 8",
        "10, 30, 3",
        "100, 20, 5"
    })
    void shouldMatchSequentialResult(int start, int count, int threads) {
        String parallel = strategy.calculate(start, count, threads);
        String sequential = PiDigits.getDigitsHex(start, count);
        
        assertEquals(sequential, parallel, 
            String.format("Mismatch for start=%d, count=%d, threads=%d", start, count, threads));
    }

    @Test
    @DisplayName("Should handle uneven division of work")
    void shouldHandleUnevenDivision() {
        // 17 digits with 4 threads = segments of 5, 4, 4, 4
        String result = strategy.calculate(0, 17, 4);
        String expected = PiDigits.getDigitsHex(0, 17);
        
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should handle more threads than digits")
    void shouldHandleMoreThreadsThanDigits() {
        String result = strategy.calculate(0, 5, 10);
        String expected = PiDigits.getDigitsHex(0, 5);
        
        assertEquals(expected, result);
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("Should not deadlock with many threads")
    void shouldNotDeadlock() {
        String result = strategy.calculate(0, 100, 20);
        
        assertNotNull(result);
        assertEquals(100, result.length());
    }

    @Test
    @DisplayName("Should return correct length of digits")
    void shouldReturnCorrectLength() {
        int count = 50;
        String result = strategy.calculate(0, count, 4);
        
        assertEquals(count, result.length());
    }

    @Test
    @DisplayName("Should work with large count and many threads")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void shouldWorkWithLargeCountAndManyThreads() {
        String result = strategy.calculate(0, 500, 8);
        String expected = PiDigits.getDigitsHex(0, 500);
        
        assertEquals(expected, result);
    }
}
