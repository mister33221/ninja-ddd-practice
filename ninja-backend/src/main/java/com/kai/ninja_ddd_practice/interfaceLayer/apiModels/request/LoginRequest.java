package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    private String username;
    private String password;

}
