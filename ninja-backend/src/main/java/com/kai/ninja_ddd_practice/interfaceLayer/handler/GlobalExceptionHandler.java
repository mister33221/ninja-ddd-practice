package com.kai.ninja_ddd_practice.interfaceLayer.handler;

import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.domainLayer.exception.DomainException;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder().customCode(null).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(e.getMessage()).build();
    }

    @ExceptionHandler(DomainException.class)
    public ErrorResponse handleDomainException(DomainException e) {
        return ErrorResponse.builder().customCode(e.getErrorCode().getCustomCode()).httpStatus(e.getErrorCode().getHttpStatusCode()).message(e.getMessage()).build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse handleApplicationException(ApplicationException e) {
        return ErrorResponse.builder().customCode(e.getErrorCode().getCustomCode()).httpStatus(e.getErrorCode().getHttpStatusCode()).message(e.getMessage()).build();
    }


}
