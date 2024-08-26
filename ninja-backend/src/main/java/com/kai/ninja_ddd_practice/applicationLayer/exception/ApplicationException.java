package com.kai.ninja_ddd_practice.applicationLayer.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationErrorCode errorCode;

    public ApplicationException(ApplicationErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationException(ApplicationErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
