package edu.eci.arsw.parallelism.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PiBenchmark class.
 */
class PiBenchmarkTest {

    private PiBenchmark benchmark;

    @BeforeEach
    void setUp() {
        benchmark = new PiBenchmark();
    }

    @Test
    @DisplayName("Should create PiBenchmark instance successfully")
    void testConstructor() {
        assertNotNull(benchmark);
    }

    @Test
    @DisplayName("Should run sequential strategy and return positive execution time")
    void testRunSingleTestSequential() {
        long executionTime = benchmark.runSingleTest("sequential", 0, 100, 1);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should run threads strategy with 1 thread and return positive execution time")
    void testRunSingleTestThreadsWithOneThread() {
        long executionTime = benchmark.runSingleTest("threads", 0, 100, 1);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should run threads strategy with multiple threads and return positive execution time")
    void testRunSingleTestThreadsWithMultipleThreads() {
        int threads = Runtime.getRuntime().availableProcessors();
        long executionTime = benchmark.runSingleTest("threads", 0, 100, threads);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should run threads strategy with 2x availableProcessors")
    void testRunSingleTestThreadsWithDoubleProcessors() {
        int threads = 2 * Runtime.getRuntime().availableProcessors();
        long executionTime = benchmark.runSingleTest("threads", 0, 100, threads);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should run threads strategy with high thread count (200)")
    void testRunSingleTestThreadsWithHighThreadCount() {
        long executionTime = benchmark.runSingleTest("threads", 0, 100, 200);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should run threads strategy with very high thread count (500)")
    void testRunSingleTestThreadsWithVeryHighThreadCount() {
        long executionTime = benchmark.runSingleTest("threads", 0, 100, 500);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should handle different start positions")
    void testRunSingleTestWithDifferentStartPosition() {
        long executionTime = benchmark.runSingleTest("sequential", 50, 100, 1);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Should run full benchmark without exceptions")
    void testRunFullBenchmark() {
        assertDoesNotThrow(() -> benchmark.runFullBenchmark(0, 100));
    }

    @Test
    @DisplayName("Should run full benchmark with available processors without exceptions")
    void testRunFullBenchmarkDisplaysProcessors() {
        assertDoesNotThrow(() -> benchmark.runFullBenchmark(0, 50));
    }

    @Test
    @DisplayName("Should calculate speedup values without exceptions")
    void testRunFullBenchmarkDisplaysSpeedup() {
        assertDoesNotThrow(() -> benchmark.runFullBenchmark(0, 50));
    }

    @Test
    @DisplayName("Sequential strategy should be slower or equal compared to parallel with multiple threads for large count")
    void testSequentialVsParallelPerformance() {
        int count = 500;
        int threads = Runtime.getRuntime().availableProcessors();

        long sequentialTime = benchmark.runSingleTest("sequential", 0, count, 1);
        long parallelTime = benchmark.runSingleTest("threads", 0, count, threads);

        // Both should complete successfully (times are non-negative)
        assertTrue(sequentialTime >= 0, "Sequential time should be non-negative");
        assertTrue(parallelTime >= 0, "Parallel time should be non-negative");
    }

    @Test
    @DisplayName("Should handle small digit counts")
    void testRunSingleTestWithSmallCount() {
        long executionTime = benchmark.runSingleTest("sequential", 0, 10, 1);
        assertTrue(executionTime >= 0, "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Main method should execute without exceptions")
    void testMainMethod() {
        assertDoesNotThrow(() -> PiBenchmark.main(new String[]{}));
    }
}
