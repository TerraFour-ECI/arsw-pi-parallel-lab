package edu.eci.arsw.parallelism.concurrency;

import edu.eci.arsw.parallelism.core.PiDigits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Sequential strategy for Pi digit calculation.
 * 
 * Calculates Pi digits sequentially without parallelization.
 * The threads parameter is ignored in this strategy.
 */
@Component
public class SequentialStrategy implements ParallelStrategy {

    private static final Logger logger = LoggerFactory.getLogger(SequentialStrategy.class);

    /**
     * Calculates Pi digits sequentially.
     * 
     * @param start starting position (0-indexed)
     * @param count number of digits to calculate
     * @param threads this parameter is ignored (sequential = 1 thread)
     * @return hexadecimal string of Pi digits
     */
    @Override
    public String calculate(int start, int count, int threads) {
        logger.debug("Sequential calculation: start={}, count={} (threads parameter {} ignored)", 
                     start, count, threads);
        
        long startTime = System.currentTimeMillis();
        
        String result = PiDigits.getDigitsHex(start, count);
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Sequential calculation completed in {}ms for {} digits", duration, count);
        
        return result;
    }

    @Override
    public String name() {
        return "sequential";
    }
}