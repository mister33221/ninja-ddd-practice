package com.kai.ninja_ddd_practice.infrastructureLayer.security.filter;

import com.kai.ninja_ddd_practice.infrastructureLayer.security.principal.JwtUserPrincipal;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * JWT 認證過濾器
 * 負責從請求中提取 JWT Token，驗證並設置 SecurityContext
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                  @NonNull HttpServletResponse response, 
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 1. 從請求中提取 JWT Token
            String token = extractTokenFromRequest(request);
            
            // 2. 如果有 Token 且有效，則設置認證資訊
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                setAuthentication(token);
            }
            
        } catch (Exception e) {
            log.error("JWT 認證過程中發生錯誤: {}", e.getMessage());
            // 不阻斷請求，讓後續的安全檢查處理
        }
        
        // 3. 繼續執行過濾器鏈
        filterChain.doFilter(request, response);
    }

    /**
     * 從請求 Header 中提取 JWT Token
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader; // JwtUtil 內部會處理 "Bearer " 前綴
        }
        return null;
    }

    /**
     * 設置 Spring Security 認證資訊
     */
    private void setAuthentication(String token) {
        try {
            // 從 JWT 中提取用戶資訊
            Map<String, Object> userInfo = jwtUtil.extractUserInfo(token);
            
            // 創建 JwtUserPrincipal
            JwtUserPrincipal principal = JwtUserPrincipal.create(
                (Long) userInfo.get("userId"),
                (String) userInfo.get("username"),
                (String) userInfo.get("email")
            );
            
            // 創建 Authentication 物件
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    principal, 
                    null, 
                    principal.getAuthorities()
                );
            
            // 設置到 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            log.debug("JWT 認證成功，用戶: {}", principal.getUsername());
            
        } catch (Exception e) {
            log.error("設置認證資訊失敗: {}", e.getMessage());
        }
    }

    /**
     * 跳過不需要認證的請求
     */    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // 跳過這些路徑的 JWT 認證
        return path.startsWith("/h2-console") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/user/login") ||
               path.equals("/user/register") ||
               path.startsWith("/test"); // 測試端點
    }
}
