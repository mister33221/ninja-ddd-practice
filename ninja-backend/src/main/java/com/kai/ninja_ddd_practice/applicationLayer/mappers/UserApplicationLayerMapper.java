package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.RegistryDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;

/**
 * 在 Application 層中的 Mapper 類別，
 * 負責
 * 1. 將 Domain 層的 Aggregate 轉換為 Application 層的 Dto。
 * 2. 將 Application 層的 Dto 轉換為 Domain 層的 Aggregate。
 */
public class UserApplicationLayerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //    Add a private constructor to hide the implicit public one.
// 添加私有構造函數以隱藏隱含的公共構造函數
    private UserApplicationLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static User convertRegistryDtoToUser(RegistryDto object) {

        return objectMapper.convertValue(object, User.class);

    }

}
