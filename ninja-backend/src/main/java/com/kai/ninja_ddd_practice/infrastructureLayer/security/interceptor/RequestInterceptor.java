package com.kai.ninja_ddd_practice.infrastructureLayer.security.interceptor;

import com.kai.ninja_ddd_practice.infrastructureLayer.security.annotations.AuthorizationValidation;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.exception.InfraSecurityErrorCode;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.exception.InfraSecurityException;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
@AllArgsConstructor
@Order(1)
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);
    private JwtUtil jwtUtil;
    private static final String JWT_PREFIX = "Bearer ";
    private static final String JWT_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        1. 先確認 handler 是不是 HandlerMethod，如果不是就直接 return true(HandlerMethod 指的是 Spring MVC 的 Controller)，放行
        if (!checkHandlerMethod(handler)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

//        2. 確認該 method 是否有 @AuthorizationValidation annotation，如果沒有就 return true 直接放行
        Method method = handlerMethod.getMethod();
        if (!checkJwtValidationAnnotation(method)) {
            log.info("Method: " + method.getName() + " does not have AuthorizationValidation annotation.");
            return true;
        }

//        3. 取得 request header 中的 jwt token
        String token = request.getHeader(JWT_HEADER);

//        3.1 如果 token 為空，就回傳 false，表示 token 無效
        if (token == null) {
            log.error("Jwt token is empty.");
            throw new InfraSecurityException(InfraSecurityErrorCode.JWT_EMPTY);
        }

//        4. 如果 token 為空或不是以 "Bearer " 開頭，就回傳 false，表示 token 無效
        if (!checkToken(token)) {
            log.error("Jwt token structure is invalid.");
            throw new InfraSecurityException(InfraSecurityErrorCode.JWT_STRUCTURE_ERROR);
        }

//        5. 將 token 去掉 "Bearer " 後，進行驗證，如果 token 無效就回傳 false
        String jwt = token.substring(JWT_PREFIX.length());
        if (!validateToken(jwt)) {
            log.error("Jwt token is invalid.");
            throw new InfraSecurityException(InfraSecurityErrorCode.JWT_INVALID);
        }

        return true;
    }

    private boolean checkHandlerMethod (Object handler) {
        return handler instanceof HandlerMethod;
    }

    private boolean checkJwtValidationAnnotation (Method method) {
        return method.isAnnotationPresent(AuthorizationValidation.class);
    }

    private boolean checkToken (String token) {
        return token != null && token.startsWith(JWT_PREFIX);
    }

    private boolean validateToken (String jwt) {
        return jwtUtil.validateToken(jwt);
    }
}
