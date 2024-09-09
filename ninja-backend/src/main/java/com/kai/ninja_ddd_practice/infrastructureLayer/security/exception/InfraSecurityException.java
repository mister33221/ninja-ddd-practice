package com.kai.ninja_ddd_practice.infrastructureLayer.security.exception;

import lombok.Getter;

@Getter
public class InfraSecurityException extends RuntimeException {
    private final InfraSecurityErrorCode errorCode;

    public InfraSecurityException(InfraSecurityErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public InfraSecurityException(InfraSecurityErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
