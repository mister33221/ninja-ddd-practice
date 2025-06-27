package com.kai.ninja_ddd_practice.infrastructureLayer.security.principal;

import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * JWT 用戶主體 - 用於 @AuthenticationPrincipal
 * 封裝從 JWT Token 解析出的用戶資訊
 */
@Value
@Builder
public class JwtUserPrincipal implements UserDetails {
    
    Long userId;
    String username;
    String email;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 目前簡化實作，實際可根據用戶角色返回權限
        return Collections.emptyList();
    }
    
    @Override
    public String getPassword() {
        // JWT 認證不需要密碼
        return null;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    /**
     * 創建 Principal 的工廠方法
     */
    public static JwtUserPrincipal create(Long userId, String username, String email) {
        return JwtUserPrincipal.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .build();
    }
}
