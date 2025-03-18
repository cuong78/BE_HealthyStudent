package com.healthy.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
@RestControllerAdvice
public class APIExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(APIExceptionHandler.class);

    @Getter
    @Setter
    private static class ErrorResponse {
        private LocalDateTime timestamp;
        private String message;

        public ErrorResponse(String message) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        logger.error("Unexpected runtime error", exception);
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Validation Errors - Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String messages = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        logger.warn("Validation error: {}", messages);
        return new ResponseEntity<>(
                new ErrorResponse(messages),
                HttpStatus.BAD_REQUEST
        );
    }

    // Duplicate Key Violation - Conflict
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(SQLIntegrityConstraintViolationException exception) {
        logger.warn("Duplicate key violation", exception);
        return new ResponseEntity<>(
                new ErrorResponse("Resource already exists or violates unique constraint"),
                HttpStatus.CONFLICT
        );
    }

    // Null Pointer - Internal Server Error
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException exception) {
        logger.error("Null pointer exception", exception);
        return new ResponseEntity<>(
                new ErrorResponse("Internal server error: Null reference"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Authorization Errors - Forbidden
    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizeException exception) {
        logger.warn("Authorization failed", exception);
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    // Operation Failed - Bad Request
    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<ErrorResponse> handleOperationFailedException(OperationFailedException exception) {
        logger.warn("Operation failed", exception);
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // Invalid Token - Unauthorized
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception) {
        logger.warn("Invalid token", exception);
        return new ResponseEntity<>(
                new ErrorResponse("Authentication failed: Invalid token"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Resource Invalid - Bad Request
    @ExceptionHandler(ResourceInvalidException.class)
    public ResponseEntity<ErrorResponse> handleResourceInvalidException(ResourceInvalidException exception) {
        logger.warn("Resource invalid", exception);
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // Resource Already Exists - Conflict
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {
        logger.warn("Resource already exists", exception);
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    // Resource Not Found - Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        logger.warn("Resource not found", exception);
        return new ResponseEntity<>(
                new ErrorResponse(exception.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}