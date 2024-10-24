package com.example.assignement.exceptions;

import com.example.assignement.domains.CustomException;
import com.example.assignement.domains.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        logger.error("Error occurred: {} - Code: {} - Path: {}", ex.getMessage(), ex.getErrorCode(), request.getRequestURI());

        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getErrorCode(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("Unexpected error: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse response = new ErrorResponse("An unexpected error occurred", "UNKNOWN_ERROR", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
