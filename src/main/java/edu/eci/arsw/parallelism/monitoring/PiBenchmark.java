package edu.eci.arsw.parallelism.monitoring;

import edu.eci.arsw.parallelism.concurrency.SequentialStrategy;
import edu.eci.arsw.parallelism.concurrency.ThreadJoinStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Benchmark class for measuring Pi digit calculation performance.
 * Compares sequential vs parallel strategies with different thread counts.
 */
public class PiBenchmark {

    private static final Logger logger = LoggerFactory.getLogger(PiBenchmark.class);

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

        logger.info("");
        logger.info("PI DIGITS BENCHMARK");
        logger.info("");
        logger.info("Start: {}, Count: {}", start, count);
        logger.info("Available Processors: {}", availableProcessors);
        logger.info("");


        long seqTime = runSingleTest("sequential", start, count, 1);
        logger.info("{} {} ms", String.format("%-35s", "Sequential"), seqTime);


        int[] threadConfigs = {1, availableProcessors, 2 * availableProcessors, 200, 500};
        String[] labels = {
                "Threads (1)",
                "Threads (availableProcessors=" + availableProcessors + ")",
                "Threads (2×availableProcessors=" + (2 * availableProcessors) + ")",
                "Threads (200)",
                "Threads (500)"
        };

        for (int i = 0; i < threadConfigs.length; i++) {
            long time = runSingleTest("threads", start, count, threadConfigs[i]);
            double speedup = (double) seqTime / time;
            logger.info("{} {} ms (Speedup: {}x)", String.format("%-35s", labels[i]), time, String.format("%.2f", speedup));
        }

        logger.info("");
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
