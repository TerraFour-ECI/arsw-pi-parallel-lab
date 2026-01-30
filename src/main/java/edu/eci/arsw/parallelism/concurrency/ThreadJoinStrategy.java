package edu.eci.arsw.parallelism.concurrency;
import edu.eci.arsw.parallelism.concurrency.ParallelStrategy;

/**
 * A strategy for calculating hexadecimal digits of Pi using multiple threads.
 */
public class ThreadJoinStrategy implements ParallelStrategy {

    /**
     * Calculates hexadecimal digits of Pi using multiple threads.
     * @param start the starting position (0-indexed) from which to begin calculating Pi digits
     * @param count the total number of hexadecimal digits to compute across all threads
     * @param threads the number of threads to use for dividing the work
     * @return a string containing the concatenated hexadecimal digits of Pi in order
     */
    @Override
    public String calculate(int start, int count, int threads) {

        int segmentSize = count / threads; // segmentos
        int remainder = count % threads;

        Thread[] workers = new Thread[threads];

        String[] results = new String[threads];

        int currentStart = start;
        for (int i = 0; i < threads; i++) { // con esto creamos los hilos
            int segmentCount = segmentSize + (i < remainder ? 1 : 0);
            final int segmentStart = currentStart;
            final int finalSegmentCount = segmentCount;
            final int index = i;

            workers[i] = new Thread(() -> {
                results[index] = calculateSegment(segmentStart, finalSegmentCount);
            });

            workers[i].start();
            currentStart += segmentCount;
        }

        for (Thread worker : workers) {
            try {
                worker.join(); // aca sincronizamos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }
        }

        StringBuilder result = new StringBuilder();
        for (String segment : results) {
            result.append(segment);
        }

        return result.toString();
    }

    /**
     * Returns the name of the strategy.
     * @return the name of the strategy
     */
    @Override
    public String name() {
        return "threads";
    }

    /**
     * Calculates a segment of hexadecimal Pi digits using the BBP algorithm.
     *
     * @param start the starting position (0-indexed) for Pi digit calculation
     * @param count the number of digits to calculate in this segment
     * @return hexadecimal string representation of the calculated Pi digits
     */
    private String calculateSegment(int start, int count) {
        return edu.eci.arsw.parallelism.core.PiDigits.getDigitsHex(start, count);
    }
}
