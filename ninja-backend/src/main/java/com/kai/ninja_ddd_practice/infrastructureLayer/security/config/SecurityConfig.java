package com.kai.ninja_ddd_practice.infrastructureLayer.security.config;

import com.kai.ninja_ddd_practice.infrastructureLayer.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置
 * 配置 JWT 認證過濾器和安全規則
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 禁用 CSRF（因為使用 JWT）
                .csrf(AbstractHttpConfigurer::disable)
                
                // 配置 CORS
                .cors(cors -> cors.configure(http))
                
                // 配置 Session 管理（無狀態）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // 配置授權規則
                .authorizeHttpRequests(auth -> auth
                        // 允許訪問的公開端點
                        .requestMatchers(
                                "/user/login",
                                "user/registry",
                                "/test/**",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/product/get-product-cards"
                        ).permitAll()
                        
                        // 其他請求需要認證
                        .anyRequest().authenticated()
                )
                
                // 添加 JWT 認證過濾器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                  // 禁用 Frame Options（允許 H2 Console）
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()))
                
                .build();
    }
}
