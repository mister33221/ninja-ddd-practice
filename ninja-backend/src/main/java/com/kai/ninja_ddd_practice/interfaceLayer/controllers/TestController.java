package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {

    private final JwtUtil jwtUtil;

    public TestController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/hello-world")
    @Operation(summary = "Hello World", description = "Hello World", tags = {"test"})
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/generate-jwt")
    @Operation(summary = "Generate JWT", description = "Generate JWT", tags = {"test"})
    public String generateJwt() {
        Map<String, Object> claims = new HashMap<>();
        return jwtUtil.generateToken(claims);
    }

}
