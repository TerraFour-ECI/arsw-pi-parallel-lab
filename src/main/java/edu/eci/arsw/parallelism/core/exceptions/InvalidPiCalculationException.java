package edu.eci.arsw.parallelism.core.exceptions;

public class InvalidPiCalculationException extends RuntimeException {
    
    private final String field;
    private final Object rejectedValue;
    
    public InvalidPiCalculationException(String message, String field, Object rejectedValue) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }
    
    public InvalidPiCalculationException(String message) {
        super(message);
        this.field = null;
        this.rejectedValue = null;
    }
    
    public String getField() {
        return field;
    }
    
    public Object getRejectedValue() {
        return rejectedValue;
    }
}