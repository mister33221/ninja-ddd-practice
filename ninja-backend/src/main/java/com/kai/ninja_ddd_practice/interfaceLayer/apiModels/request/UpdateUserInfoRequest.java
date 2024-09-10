package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpdateUserInfoRequest {
    private String username;
    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    private String address;
}
