package com.kai.ninja_ddd_practice.interfaceLayer.handler;

import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.domainLayer.exception.DomainException;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(1)
public class GlobalExceptionHandler {

//    當你在其他地方拋出Exception時，會被這邊攔截，但下面我又有針對特定的錯誤類型做攔截，會造成重複攔截的問題
//    @ExceptionHandler(Exception.class)
//    public ErrorResponse handleException(Exception e) {
//        return ErrorResponse.builder().customCode(null).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(e.getMessage()).build();
//    }

//    我本來是使用我自製的 ErrorResponse 回給前端，但是spring boot 總是複寫我的http status code，
//    且要在 application.yaml那邊把 spring.mvc.throw-exception-if-no-handler-found 設為 false 才能看到我自己的錯誤訊息，
//    不知道原因，
//    後來我改用 ResponseEntity 來回傳，就可以正確的回傳我想要的http status code
//    @ExceptionHandler(DomainException.class)
//    public ErrorResponse handleDomainException(DomainException e) {
//        return ErrorResponse.builder().customCode(e.getErrorCode().getCustomCode()).httpStatus(e.getErrorCode().getHttpStatusCode()).message(e.getMessage()).build();
//    }
//
//    @ExceptionHandler(ApplicationException.class)
//    public ErrorResponse handleApplicationException(ApplicationException e) {
//        return ErrorResponse.builder().customCode(e.getErrorCode().getCustomCode()).httpStatus(e.getErrorCode().getHttpStatusCode()).message(e.getMessage()).build();
//    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode()).body(ErrorResponse.builder().customCode(e.getErrorCode().getCustomCode()).httpStatus(e.getErrorCode().getHttpStatusCode()).message(e.getMessage()).build());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode()).body(ErrorResponse.builder().customCode(e.getErrorCode().getCustomCode()).httpStatus(e.getErrorCode().getHttpStatusCode()).message(e.getMessage()).build());
    }

}
