package com.stephen.spring_boot_api.exception;

public enum ErrorCode {
    USER_EXITED(1001, "User existed"),
    UNCATEGORIZED_EXCEPTION(1002, "Uncategorized exception"),
    USER_NOT_EXISTED(1003, "User not existed");

    // constructor
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
