package com.stephen.spring_boot_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXITED(1001, "User existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(1002, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1003, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You don't have permission", HttpStatus.FORBIDDEN);

    // constructor
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
