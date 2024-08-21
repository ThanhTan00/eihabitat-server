package com.eihabitat.eihabitat_server.exception;

public enum ErrorCode {
    USER_EXISTED( 1001, "User already exists"),
    UNCATEGORIZED_EXCEPTION( 9999, "Uncategorized exception"),
    USER_PASSWORD_INVALID( 1002, "User password must be at least 8 characters"),
    INVALID_KEY( 1000, "Invalid message key"),
    PROFILE_NAME_INVALID( 1003, "Profile name must be at least 6 characters"),
    EMAIL_INVALID( 1004, "Email invalid"),
    USER_NOT_FOUND( 1005, "User not found"),
    USER_NOT_EXISTED( 1006, "User not exists"),
    USER_UNAUTHENTICATED( 1007, "User unauthenticated"),

    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
