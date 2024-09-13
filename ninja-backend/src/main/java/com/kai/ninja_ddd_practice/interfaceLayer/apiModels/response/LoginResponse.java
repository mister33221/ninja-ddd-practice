package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String token;

}
