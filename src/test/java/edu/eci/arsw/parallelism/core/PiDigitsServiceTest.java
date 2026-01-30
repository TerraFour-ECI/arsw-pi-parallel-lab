package edu.eci.arsw.parallelism.core;

import edu.eci.arsw.parallelism.concurrency.SequentialStrategy;
import edu.eci.arsw.parallelism.concurrency.ThreadJoinStrategy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.eci.arsw.parallelism.core.exceptions.InvalidPiCalculationException;

@DisplayName("PiDigitsService Tests")
class PiDigitsServiceTest {

    private PiDigitsService service;

    @BeforeEach
    void setUp() {
        SequentialStrategy sequentialStrategy = new SequentialStrategy();
        ThreadJoinStrategy threadJoinStrategy = new ThreadJoinStrategy();
        service = new PiDigitsService(sequentialStrategy, threadJoinStrategy);
    }

    // ========== Happy Path Tests ==========

    @Test
    @DisplayName("Should calculate Pi digits successfully with valid inputs")
    void testCalculateSequentialValidInput() {
        String result = service.calculateSequential(0, 5);

        assertNotNull(result, "Result should not be null");
        assertEquals(5, result.length(), "Result length should match count");
        assertEquals("243F6", result, "First 5 digits of Pi should be 243F6");
        assertTrue(result.matches("[0-9A-F]+"), "Result should contain only hex digits");
    }

    @Test
    @DisplayName("Should calculate single digit")
    void testCalculateSequentialSingleDigit() {
        String result = service.calculateSequential(0, 1);

        assertNotNull(result);
        assertEquals(1, result.length());
        assertEquals("2", result);
    }

    @ParameterizedTest
    @DisplayName("Should calculate Pi digits from different starting positions")
    @CsvSource({
            "0, 5, 243F6",
            "5, 5, A8885",
            "10, 3, A30",
            "100, 4, 29B7"
    })
    void testCalculateSequentialDifferentPositions(int start, int count, String expected) {
        String result = service.calculateSequential(start, count);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should calculate large number of digits")
    void testCalculateSequentialLargeCount() {
        String result = service.calculateSequential(0, 100);

        assertNotNull(result);
        assertEquals(100, result.length());
        assertTrue(result.matches("[0-9A-F]+"));
    }

    // ========== Validation Tests - Negative Cases ==========

    @Test
    @DisplayName("Should throw exception for negative start position")
    void testCalculateSequentialNegativeStart() {
        InvalidPiCalculationException exception = assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(-1, 5));

        assertTrue(exception.getMessage().contains("non-negative"));
        assertEquals("start", exception.getField());
        assertEquals(-1, exception.getRejectedValue());
    }

    @Test
    @DisplayName("Should throw exception for zero count")
    void testCalculateSequentialZeroCount() {
        InvalidPiCalculationException exception = assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(0, 0));

        assertTrue(exception.getMessage().contains("positive"));
        assertEquals("count", exception.getField());
        assertEquals(0, exception.getRejectedValue());
    }

    @Test
    @DisplayName("Should throw exception for negative count")
    void testCalculateSequentialNegativeCount() {
        InvalidPiCalculationException exception = assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(0, -10));

        assertTrue(exception.getMessage().contains("positive"));
        assertEquals("count", exception.getField());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for various invalid start values")
    @ValueSource(ints = { -1, -100, -1000, Integer.MIN_VALUE })
    void testCalculateSequentialVariousNegativeStarts(int invalidStart) {
        assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(invalidStart, 5));
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for various invalid count values")
    @ValueSource(ints = { 0, -1, -100, Integer.MIN_VALUE })
    void testCalculateSequentialVariousInvalidCounts(int invalidCount) {
        assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(0, invalidCount));
    }

    // ========== Boundary Tests ==========

    @Test
    @DisplayName("Should throw exception when start exceeds maximum")
    void testCalculateSequentialStartExceedsMax() {
        int maxStart = service.getMaxStart();

        InvalidPiCalculationException exception = assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(maxStart + 1, 5));

        assertTrue(exception.getMessage().contains("exceeds maximum"));
        assertEquals("start", exception.getField());
    }

    @Test
    @DisplayName("Should throw exception when count exceeds maximum")
    void testCalculateSequentialCountExceedsMax() {
        int maxCount = service.getMaxCount();

        InvalidPiCalculationException exception = assertThrows(
                InvalidPiCalculationException.class,
                () -> service.calculateSequential(0, maxCount + 1));

        assertTrue(exception.getMessage().contains("exceeds maximum"));
        assertEquals("count", exception.getField());
    }

    @Test
    @DisplayName("Should calculate successfully at maximum allowed start")
    void testCalculateSequentialAtMaxStart() {
        int maxStart = service.getMaxStart();

        String result = service.calculateSequential(maxStart, 5);

        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    @DisplayName("Should calculate successfully at maximum allowed count")
    void testCalculateSequentialAtMaxCount() {
        int maxCount = service.getMaxCount();

        String result = service.calculateSequential(0, maxCount);

        assertNotNull(result);
        assertEquals(maxCount, result.length());
    }

    // ========== Determinism Tests ==========

    @Test
    @DisplayName("Should return consistent results for same inputs")
    void testCalculateSequentialDeterminism() {
        String result1 = service.calculateSequential(10, 50);
        String result2 = service.calculateSequential(10, 50);
        String result3 = service.calculateSequential(10, 50);

        assertEquals(result1, result2, "First and second calls should match");
        assertEquals(result2, result3, "Second and third calls should match");
        assertEquals(result1, result3, "First and third calls should match");
    }

    @Test
    @DisplayName("Should produce consistent results across different ranges")
    void testCalculateSequentialConsistencyAcrossRanges() {
        // Get digits 0-9
        String fullRange = service.calculateSequential(0, 10);

        // Get digits 0-4 and 5-9 separately
        String firstPart = service.calculateSequential(0, 5);
        String secondPart = service.calculateSequential(5, 5);

        assertEquals(fullRange, firstPart + secondPart,
                "Concatenated parts should equal full range");
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Should handle boundary between different calculation segments")
    void testCalculateSequentialSegmentBoundary() {
        // Test around DIGITS_PER_SUM boundary (8 digits)
        String result7 = service.calculateSequential(0, 7);
        String result8 = service.calculateSequential(0, 8);
        String result9 = service.calculateSequential(0, 9);

        assertNotNull(result7);
        assertNotNull(result8);
        assertNotNull(result9);

        // Verify the 9-digit result contains the 8-digit result
        assertTrue(result9.startsWith(result8));
        assertTrue(result8.startsWith(result7));
    }


    // ========== Phase 1 - calculateWithStrategy Tests ==========
    

    @Test
    @DisplayName("Should default to sequential when strategy is null")
    void testCalculateWithStrategyDefaultsToSequential() {
        String result = service.calculateWithStrategy(0, 5, null, null);
        assertEquals("243F6", result);
    }
    
 
    @Test
    @DisplayName("Should work with explicit sequential strategy")
    void testCalculateWithStrategySequential() {
        String result = service.calculateWithStrategy(0, 5, null, "sequential");
        assertEquals("243F6", result);
    }
    

    @Test
    @DisplayName("Should throw exception for invalid strategy")
    void testCalculateWithStrategyInvalid() {
        InvalidPiCalculationException exception = assertThrows(
            InvalidPiCalculationException.class,
            () -> service.calculateWithStrategy(0, 5, 4, "invalid")
        );
        assertEquals("strategy", exception.getField());
    }
    

    @Test
    @DisplayName("Should require threads parameter when using threads strategy")
    void testCalculateWithStrategyThreadsRequired() {
        InvalidPiCalculationException exception = assertThrows(
            InvalidPiCalculationException.class,
            () -> service.calculateWithStrategy(0, 5, null, "threads")
        );
        assertEquals("threads", exception.getField());
    }
    

    @Test
    @DisplayName("Should throw exception for threads <= 0")
    void testCalculateWithStrategyThreadsInvalid() {
        assertThrows(InvalidPiCalculationException.class,
            () -> service.calculateWithStrategy(0, 5, 0, "threads"));
        assertThrows(InvalidPiCalculationException.class,
            () -> service.calculateWithStrategy(0, 5, -5, "threads"));
    }
    

    @Test
    @DisplayName("Should throw exception for threads exceeding maximum")
    void testCalculateWithStrategyThreadsExceedsMax() {
        InvalidPiCalculationException exception = assertThrows(
            InvalidPiCalculationException.class,
            () -> service.calculateWithStrategy(0, 5, 201, "threads")
        );
        assertTrue(exception.getMessage().contains("200"));
    }
    

    @Test
    @DisplayName("Should accept threads strategy with valid threads (fallback)")
    void testCalculateWithStrategyThreadsValid() {
        String result = service.calculateWithStrategy(0, 5, 4, "threads");
        assertEquals("243F6", result); 
    }
    
    @Test
    @DisplayName("Should return same result for sequential and threads (fallback)")
    void testCalculateWithStrategyEquivalence() {
        String sequential = service.calculateSequential(0, 10);
        String withThreads = service.calculateWithStrategy(0, 10, 4, "threads");
        assertEquals(sequential, withThreads);
    }

}
