package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.applicationService.UserApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.RegistryDto;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.LoginRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.RegistryRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.LoginResponse;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.RegistryResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserApplicationService userService;
//    jscksin object mapper
    private final ObjectMapper objectMapper;

    public UserController(UserApplicationService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/registry")
    @Operation(summary = "Register a new user", description = "Register a new user", tags = {"User"})
    public ResponseEntity<RegistryResponse> registry(@RequestBody RegistryRequest request) {
//        轉換為 application layer 的 dto
        RegistryDto registryDto = objectMapper.convertValue(request, RegistryDto.class);
        String message = userService.registry(registryDto);
        return ResponseEntity.ok(RegistryResponse.builder().message(message).build());
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login", tags = {"User"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String message = userService.login(request);
        return ResponseEntity.ok(LoginResponse.builder().token(message).build());
    }
}
