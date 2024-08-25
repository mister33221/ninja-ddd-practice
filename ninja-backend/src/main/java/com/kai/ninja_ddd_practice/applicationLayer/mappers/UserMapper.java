package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserCredentials;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserProfile;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.RegistryRequest;

public class UserMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //    Add a private constructor to hide the implicit public one.
// 添加私有構造函數以隱藏隱含的公共構造函數
    private UserMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static User convertRegistryRequestToUser(RegistryRequest object) {
        new User();
        new UserProfile();
        new UserCredentials();
        return User.builder()
                .username(object.getUsername())
                .profile(UserProfile.builder()
                        .fullName(object.getFullName())
                        .email(object.getEmail())
                        .phoneNumber(object.getPhoneNumber())
                        .dateOfBirth(object.getDateOfBirth())
                        .address(object.getAddress())
                        .build())
                .credentials(UserCredentials.builder()
                        .hashedPassword(object.getPassword())
                        .build())
                .build();


    }

}
