
package edu.eci.arsw.parallelism.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.eci.arsw.parallelism.core.exceptions.InvalidPiCalculationException;
import edu.eci.arsw.parallelism.core.exceptions.PiCalculationTimeoutException;

@Service
public class PiDigitsService {

    private static final Logger logger = LoggerFactory.getLogger(PiDigitsService.class);
    private static final int MAX_COUNT = 10_000; // 10 thousand digits max
    private static final int MAX_START = 100_000; // 100 thousand position max
    private static final long TIMEOUT_MILLIS = 30_000; // Seconds timeout
    //TODO: Consider when implementing async job processing, to increse these values to see how it performs

    /**
     * Calculates Pi digits sequentially with comprehensive validation.
     *
     * @param start 0-based position after the radix point (must be >= 0)
     * @param count number of digits to return (must be > 0)
     * @return hexadecimal string of Pi digits
     * @throws InvalidPiCalculationException if parameters are invalid
     * @throws PiCalculationTimeoutException if calculation exceeds timeout
     */

    public String calculateSequential(int start, int count) {
        logger.debug("Calculating Pi digits: start={}, count={}", start, count);
        
        // Validate inputs
        validateInputs(start, count);
        
        // Perform calculation with timeout monitoring
        long startTime = System.currentTimeMillis();
        
        try {
            String result = PiDigits.getDigitsHex(start, count);
            
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Pi calculation completed: start={}, count={}, time={}ms", 
                       start, count, elapsedTime);
            
            // Check if operation took too long (defensive check)
            if (elapsedTime > TIMEOUT_MILLIS) {
                throw new PiCalculationTimeoutException(
                    "Calculation exceeded maximum allowed time", elapsedTime);
            }
            
            return result;
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument in PiDigits calculation: {}", e.getMessage());
            throw new InvalidPiCalculationException(
                "Failed to calculate Pi digits: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during Pi calculation", e);
            throw new RuntimeException("Unexpected error during Pi calculation", e);
        }
    }

    /**
     * Validates input parameters for Pi digit calculation.
     *
     * @param start starting position
     * @param count number of digits
     * @throws InvalidPiCalculationException if validation fails
     */
    private void validateInputs(int start, int count) {
        if (start < 0) {
            throw new InvalidPiCalculationException(
                "Start position must be non-negative", "start", start);
        }
        
        if (count <= 0) {
            throw new InvalidPiCalculationException(
                "Count must be positive", "count", count);
        }
        
        if (start > MAX_START) {
            throw new InvalidPiCalculationException(
                String.format("Start position exceeds maximum allowed value of %d", MAX_START),
                "start", start);
        }
        
        if (count > MAX_COUNT) {
            throw new InvalidPiCalculationException(
                String.format("Count exceeds maximum allowed value of %d", MAX_COUNT),
                "count", count);
        }
        
        // Check for potential overflow when start + count
        if ((long) start + (long) count > Integer.MAX_VALUE) {
            throw new InvalidPiCalculationException(
                "Start + count would cause overflow", "start+count", 
                (long) start + (long) count);
        }
    }

    /**
     * Returns the maximum allowed count for Pi digit calculation.
     */
    public int getMaxCount() {
        return MAX_COUNT;
    }

    /**
     * Returns the maximum allowed start position for Pi digit calculation.
     */
    public int getMaxStart() {
        return MAX_START;
    }
}
