package com.stephen.spring_boot_api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// catch exception globally
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
