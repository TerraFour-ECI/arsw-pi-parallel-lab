package edu.eci.arsw.parallelism.monitoring;

import edu.eci.arsw.parallelism.concurrency.SequentialStrategy;
import edu.eci.arsw.parallelism.concurrency.ThreadJoinStrategy;

/**
 * Benchmark class for measuring Pi digit calculation performance.
 * Compares sequential vs parallel strategies with different thread counts.
 */
public class PiBenchmark {

    private final SequentialStrategy sequentialStrategy;
    private final ThreadJoinStrategy threadJoinStrategy;

    public PiBenchmark() {
        this.sequentialStrategy = new SequentialStrategy();
        this.threadJoinStrategy = new ThreadJoinStrategy();
    }

    /**
     * Runs a single benchmark test and returns execution time in milliseconds.
     *
     * @param strategy "sequential" or "threads"
     * @param start starting position
     * @param count number of digits
     * @param threads number of threads (ignored for sequential)
     * @return execution time in milliseconds
     */
    public long runSingleTest(String strategy, int start, int count, int threads) {
        long startTime = System.currentTimeMillis();

        if ("sequential".equals(strategy)) {
            sequentialStrategy.calculate(start, count, 1);
        } else {
            threadJoinStrategy.calculate(start, count, threads);
        }

        return System.currentTimeMillis() - startTime;
    }

    /**
     * Runs the complete benchmark suite as specified in the README.
     *
     * @param start starting position
     * @param count number of digits to calculate
     */
    public void runFullBenchmark(int start, int count) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        System.out.println();
        System.out.println("PI DIGITS BENCHMARK");
        System.out.println();
        System.out.println("Start: " + start + ", Count: " + count);
        System.out.println("Available Processors: " + availableProcessors);
        System.out.println();


        long seqTime = runSingleTest("sequential", start, count, 1);
        System.out.printf("%-35s %10d ms%n", "Sequential", seqTime); // Sequential execution


        int[] threadConfigs = {1, availableProcessors, 2 * availableProcessors, 200, 500};
        String[] labels = {
                "Threads (1)",
                "Threads (availableProcessors=" + availableProcessors + ")",
                "Threads (2×availableProcessors=" + (2 * availableProcessors) + ")",
                "Threads (200)",
                "Threads (500)"
        }; // Threads execution

        for (int i = 0; i < threadConfigs.length; i++) {
            long time = runSingleTest("threads", start, count, threadConfigs[i]);
            double speedup = (double) seqTime / time;
            System.out.printf("%-35s %10d ms (Speedup: %.2fx)%n", labels[i], time, speedup);
        }

        System.out.println();
    }

    /**
     * Main method to execute the benchmark.
     */
    public static void main(String[] args) {
        PiBenchmark benchmark = new PiBenchmark();

        int start = 0;
        int count = 10000;

        benchmark.runFullBenchmark(start, count);
    }
}
