package edu.eci.arsw.parallelism.concurrency;

/**
 * Strategy interface for parallel Pi digit calculation.
 */
public interface ParallelStrategy {

    /**
     * Calculates Pi digits using this parallelism strategy.
     *
     * @param start starting position (0-indexed)
     * @param count number of digits to calculate
     * @param threads number of threads to use
     * @return String with the calculated digits
     */
    String calculate(int start, int count, int threads);

    /**
     * Returns the identifying name of the strategy.
     *
     * @return strategy name (e.g., "sequential", "threads")
     */
    String name();
}
