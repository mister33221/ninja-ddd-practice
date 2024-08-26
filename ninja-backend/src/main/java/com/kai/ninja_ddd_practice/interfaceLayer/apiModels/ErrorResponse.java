package com.kai.ninja_ddd_practice.interfaceLayer.apiModels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse  {
    private String customCode;
    private Integer httpStatus;
    private String message;

//
}
