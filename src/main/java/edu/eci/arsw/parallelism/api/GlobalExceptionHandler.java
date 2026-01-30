package edu.eci.arsw.parallelism.api;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for REST controllers.
 * This class provides centralized exception handling across all @RequestMapping methods
 * through @ExceptionHandler methods. It standardizes error responses with consistent
 * structure including timestamp, status code, error type, and message.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles constraint validation exceptions.
     * This method is triggered when bean validation constraints are violated.
     *
     * @param ex the ConstraintViolationException containing validation error details
     * @return ResponseEntity with BAD_REQUEST status and error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ConstraintViolationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation error: " + ex.getMessage());
    }

    /**
     * Handles missing required request parameter exceptions.
     * This method is triggered when a required parameter is not provided in the request.
     *
     * @param ex the MissingServletRequestParameterException with parameter details
     * @return ResponseEntity with BAD_REQUEST status and missing parameter information
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing parameter: " + ex.getParameterName());
    }

    /**
     * Handles type mismatch exceptions for method arguments.
     * This method is triggered when a request parameter cannot be converted to the expected type.
     *
     * @param ex the MethodArgumentTypeMismatchException with type conversion details
     * @return ResponseEntity with BAD_REQUEST status and type mismatch information
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid parameter type: " + ex.getName());
    }

    /**
     * Handles illegal argument exceptions.
     * This method is triggered when an invalid argument is passed to a method,
     * typically from business logic validation.
     *
     * @param ex the IllegalArgumentException with the validation error message
     * @return ResponseEntity with BAD_REQUEST status and the exception message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles generic exceptions.
     * This method acts as a fallback handler for any unhandled exceptions,
     * providing a generic error response to avoid exposing sensitive information.
     *
     * @param ex the Exception that was not handled by other specific handlers
     * @return ResponseEntity with INTERNAL_SERVER_ERROR status and generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    /**
     * Builds a standardized error response entity.
     * Creates a consistent response structure with timestamp, HTTP status code,
     * error reason phrase, and custom error message.
     *
     * @param status the HTTP status to be returned
     * @param message the custom error message to include in the response
     * @return ResponseEntity containing a map with error details
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message));
    }

    /**
     * Handles method validation exceptions.
     * This method is triggered when method-level validation fails,
     * typically on controller method parameters annotated with validation constraints.
     *
     * @param ex the HandlerMethodValidationException with validation failure details
     * @return ResponseEntity with BAD_REQUEST status and validation failure message
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMethodValidation(
            HandlerMethodValidationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Validation failed: " + ex.getMessage());
    }
}
