package com.kai.ninja_ddd_practice.domainLayer.exception;

import org.springframework.http.HttpStatus;

public enum DomainErrorCode {

    USER_NOT_FOUND("ERR_0001", HttpStatus.NOT_FOUND.value(), "User not found"),
    USERNAME_ALREADY_EXISTS("ERR_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
    EMAIL_ALREADY_EXISTS("ERR_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
    INVALID_INPUT("ERR_0004", HttpStatus.BAD_REQUEST.value(), "Invalid input"),
    INTERNAL_SERVER_ERROR("ERR_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");

    private final String customCode;
    private final Integer httpStatusCode;
    private final String message;

    DomainErrorCode(String customCode, Integer httpStatusCode, String message) {
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
