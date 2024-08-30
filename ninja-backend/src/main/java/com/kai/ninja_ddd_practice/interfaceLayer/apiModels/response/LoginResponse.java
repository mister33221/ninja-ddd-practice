package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginResponse {

    private String token;

}
