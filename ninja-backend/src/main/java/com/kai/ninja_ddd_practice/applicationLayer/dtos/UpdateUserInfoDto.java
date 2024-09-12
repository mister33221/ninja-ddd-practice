package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInfoDto {
    private Long userId;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String dateOfBirth;
    private String address;
}
