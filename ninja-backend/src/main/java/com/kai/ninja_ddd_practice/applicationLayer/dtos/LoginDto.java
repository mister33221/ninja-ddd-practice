package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    private String username;
    private String password;

}
