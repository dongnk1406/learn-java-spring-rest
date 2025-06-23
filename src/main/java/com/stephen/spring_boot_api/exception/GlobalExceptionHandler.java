package com.stephen.spring_boot_api.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.stephen.spring_boot_api.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;

// catch exception globally and global manage exception
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException exception) {
        var constrainViolation = exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
        Map<String, Object> attributes = constrainViolation.getConstraintDescriptor().getAttributes();

        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(
                constrainViolation.getMessage() != null ? mapAttribute(constrainViolation.getMessage(), attributes)
                        : exception.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ApiResponse apiResponse = new ApiResponse<>();
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        apiResponse.setCode(ErrorCode.UNAUTHORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHORIZED.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String MIN_DOB_ATTRIBUTE = "min";
        String MAX_DOB_ATTRIBUTE = "max";

        String minDob = attributes.get(MIN_DOB_ATTRIBUTE).toString();
        String maxDob = attributes.get(MAX_DOB_ATTRIBUTE).toString();

        return message.replace("{" + MIN_DOB_ATTRIBUTE + "}", minDob).replace("{" + MAX_DOB_ATTRIBUTE + "}", maxDob);
    }
}
