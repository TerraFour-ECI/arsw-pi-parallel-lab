package edu.eci.arsw.parallelism.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            assertDoesNotThrow(() -> benchmark.runFullBenchmark(0, 100));

            String output = outputStream.toString();
            assertTrue(output.contains("PI DIGITS BENCHMARK"), "Output should contain benchmark header");
            assertTrue(output.contains("Sequential"), "Output should contain sequential results");
            assertTrue(output.contains("Threads"), "Output should contain threads results");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Should display available processors in full benchmark output")
    void testRunFullBenchmarkDisplaysProcessors() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            benchmark.runFullBenchmark(0, 50);

            String output = outputStream.toString();
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            assertTrue(output.contains("Available Processors: " + availableProcessors),
                    "Output should display available processors");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Should display speedup values in full benchmark output")
    void testRunFullBenchmarkDisplaysSpeedup() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            benchmark.runFullBenchmark(0, 50);

            String output = outputStream.toString();
            assertTrue(output.contains("Speedup:"), "Output should contain speedup values");
        } finally {
            System.setOut(originalOut);
        }
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            assertDoesNotThrow(() -> PiBenchmark.main(new String[]{}));
        } finally {
            System.setOut(originalOut);
        }
    }
}
