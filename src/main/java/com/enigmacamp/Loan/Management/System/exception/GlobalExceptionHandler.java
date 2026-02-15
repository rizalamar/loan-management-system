package com.enigmacamp.Loan.Management.System.exception;

import com.enigmacamp.Loan.Management.System.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .error("Not found")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Handle 409 / Conflict
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
            DuplicateResourceException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .error("Conflict")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // handle 400 / Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .error("Bad Request")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle 401 / Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .error("Unauthorized")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Handle Bad Credentials (401) - login failed
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid Username or password")
                .error("Unauthorized")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Handle Access Denied / 403 - User doesn't have permission
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message("You don't have permission to access this resource")
                .error("Forbidden")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    // Handle Validation Errors (400) - @Valid annotation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        Map<String, String> validationError = new HashMap<>();

        // Extract all field errors
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationError.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .error("Bad Request")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle RuntimeException (500) - catch-all for unexpected errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .error("Internal Server Error")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Handle Exception (500) - catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception e,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .error("Internal Server Error")
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
