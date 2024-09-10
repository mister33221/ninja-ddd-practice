package com.kai.ninja_ddd_practice.interfaceLayer.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateUserInfoDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdateUserInfoRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetUserInfoByIdResponse;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;

public class UserControllerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private UserControllerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static GetUserInfoByIdResponse convertUserToGetUserInfoByIdResponse(User user) {
        ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("email", user.getProfile().getEmail());
        jsonNode.put("username", user.getUsername());
        jsonNode.put("fullName", user.getProfile().getFullName());
        jsonNode.put("phoneNumber", user.getProfile().getPhoneNumber());
        jsonNode.put("address", user.getProfile().getAddress());

        GetUserInfoByIdResponse response = objectMapper.convertValue(jsonNode, GetUserInfoByIdResponse.class);
//        因為 dateOfBirth 的型別是 LocalDate，如果在 convertValue 之前，就把 dateOfBirth 資料放進去給他轉，會出現錯誤(即使本來就是 localDate 轉 LocalDate)
//        所以在 covertValue 之後再放進去
        jsonNode.put("dateOfBirth", user.getProfile().getDateOfBirth().toString());
//        另外，在 GetUserInfoByIdResponse 中的 dateOfBirth 要加上 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 才能正確轉換，不然他會變成一個陣列物件
        response.setDateOfBirth(LocalDate.parse(jsonNode.get("dateOfBirth").asText()));
        return response;
    }

    public static UpdateUserInfoDto covertUpdateUserInfoRequestToDto(UpdateUserInfoRequest reques) {
        return objectMapper.convertValue(reques, UpdateUserInfoDto.class);
    }
}
