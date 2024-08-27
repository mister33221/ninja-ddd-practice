package com.kai.ninja_ddd_practice.infrastructureLayer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;


@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Ninja DDD Practice",
                version = "1.0.0",
                description = "Ninja DDD Practice"
        )
)
//@SecuritySchemes({
//        @SecurityScheme(
//                name = "bearer-jwt",
//                type = SecuritySchemeType.HTTP,
//                scheme = "bearer",
//                bearerFormat = "JWT"
//        )
//})

public class OpenApiConfig {

}
