package edu.eci.arsw.parallelism.core.exceptions;

public class PiCalculationTimeoutException extends RuntimeException {
    
    private final long elapsedMillis;
    
    public PiCalculationTimeoutException(String message, long elapsedMillis) {
        super(message);
        this.elapsedMillis = elapsedMillis;
    }
    
    public long getElapsedMillis() {
        return elapsedMillis;
    }
}