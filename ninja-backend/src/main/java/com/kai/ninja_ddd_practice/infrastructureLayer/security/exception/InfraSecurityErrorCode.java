package com.kai.ninja_ddd_practice.infrastructureLayer.security.exception;

import org.springframework.http.HttpStatus;

public enum InfraSecurityErrorCode {

    JWT_STRUCTURE_ERROR("ERR_INFRA_0001", HttpStatus.UNAUTHORIZED.value(), "JWT 結構錯誤"),
    JWT_EXPIRED("ERR_INFRA_0002", HttpStatus.UNAUTHORIZED.value(), "JWT 已過期"),
    JWT_INVALID("ERR_INFRA_0003", HttpStatus.UNAUTHORIZED.value(), "JWT 無效"),
    JWT_SIGNATURE_VERIFICATION_FAILED("ERR_INFRA_0004", HttpStatus.UNAUTHORIZED.value(), "JWT 簽名驗證失敗"),
    JWT_EMPTY("ERR_INFRA_0005", HttpStatus.UNAUTHORIZED.value(), "JWT 為空");

    private final String customCode;
    private final Integer httpStatusCode;
    private final String message;

    InfraSecurityErrorCode(String customCode, Integer httpStatusCode, String message) {
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
