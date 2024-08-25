package com.kai.ninja_ddd_practice.interfaceLayer.apiModels;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorResponse {
    private String customCode;
    private Integer httpStatus;
    private String message;
}
