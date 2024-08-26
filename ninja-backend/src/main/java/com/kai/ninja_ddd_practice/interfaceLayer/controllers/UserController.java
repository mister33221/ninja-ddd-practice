package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.UserApplicationService;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.RegistryRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.RegistryResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserApplicationService userService;

    public UserController(UserApplicationService userService) {
        this.userService = userService;
    }

    @PostMapping("/registry")
    @Operation(summary = "Register a new user", description = "Register a new user", tags = {"User"})
    public ResponseEntity<RegistryResponse> registry(@RequestBody RegistryRequest request) {
        String message = userService.registry(request);
        return ResponseEntity.ok(RegistryResponse.builder().message(message).build());
    }

}
