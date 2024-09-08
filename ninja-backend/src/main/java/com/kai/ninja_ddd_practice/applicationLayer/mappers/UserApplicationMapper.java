package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.RegistryDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserCredentials;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserProfile;

public class UserApplicationMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //    Add a private constructor to hide the implicit public one.
// 添加私有構造函數以隱藏隱含的公共構造函數
    private UserApplicationMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static User convertRegistryDtoToUser(RegistryDto object) {

        return objectMapper.convertValue(object, User.class);

    }

}
