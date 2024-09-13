package com.kai.ninja_ddd_practice.applicationLayer.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationErrorCode {
//    TODO: Fix to application error codes
    USER_NOT_FOUND("ERR_APPLICATION_0001", HttpStatus.UNAUTHORIZED.value(), "User not found"),
    USERNAME_ALREADY_EXISTS("ERR_APPLICATION_0002", HttpStatus.BAD_REQUEST.value(), "Username already exists"),
    EMAIL_ALREADY_EXISTS("ERR_APPLICATION_0003", HttpStatus.BAD_REQUEST.value(), "Email already exists"),
    INTERNAL_SERVER_ERROR("ERR_APPLICATION_0005", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"),
    SECURITY_EXCEPTION("ERR_APPLICATION_0006", HttpStatus.UNAUTHORIZED.value(), "Signature verification failed"),
    EXPIRED_JWT_TOKEN("ERR_APPLICATION_0007", HttpStatus.UNAUTHORIZED.value(), "Expired JWT token"),
    UNSUPPORTED_JWT_TOKEN("ERR_APPLICATION_0008", HttpStatus.UNAUTHORIZED.value(), "Unsupported JWT token"),
    ILLIGAL_ARGUMENT("ERR_APPLICATION_0009", HttpStatus.BAD_REQUEST.value(), "Illegal argument"),
    INVALID_PASSWORD("ERR_APPLICATION_0010", HttpStatus.UNAUTHORIZED.value(), "Invalid password"),
    PRODUCT_NOT_FOUND("ERR_APPLICATION_0011", HttpStatus.NOT_FOUND.value(), "Product not found"),
    SHOPPING_CART_NOT_FOUND("ERR_APPLICATION_0012", HttpStatus.NOT_FOUND.value(), "Shopping cart not found");

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
