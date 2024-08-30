package com.kai.ninja_ddd_practice.applicationLayer.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationErrorCode {
//    TODO: Fix to application error codes
    USER_NOT_FOUND("ERR_0001", HttpStatus.UNAUTHORIZED.value(), "User not found"),
    USERNAME_ALREADY_EXISTS("ERR_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
    EMAIL_ALREADY_EXISTS("ERR_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
    INTERNAL_SERVER_ERROR("ERR_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"),
    SECURITY_EXCEPTION("ERR_0006", HttpStatus.UNAUTHORIZED.value(), "Signature verification failed"),
    EXPIRED_JWT_TOKEN("ERR_0007", HttpStatus.UNAUTHORIZED.value(), "Expired JWT token"),
    UNSUPPORTED_JWT_TOKEN("ERR_0008", HttpStatus.UNAUTHORIZED.value(), "Unsupported JWT token"),
    ILLIGAL_ARGUMENT("ERR_0009", HttpStatus.BAD_REQUEST.value(), "Illegal argument"),
    INVALID_PASSWORD("ERR_0010", HttpStatus.UNAUTHORIZED.value(), "Invalid password");


    private final String customCode;
    private final Integer httpStatusCode;
    private final String message;

    ApplicationErrorCode(String customCode, Integer httpStatusCode, String message) {
        this.customCode = customCode;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public String getCustomCode() {
        return customCode;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

}
