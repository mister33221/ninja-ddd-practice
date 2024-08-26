package com.kai.ninja_ddd_practice.applicationLayer.util;

import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationErrorCode;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String webBackendSecret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.token-prefix}")
    private String prefix;

    @Value("${jwt.header-string}")
    private String headerString;

    private Key getSigningKey() {
        byte[] keyBytes = webBackendSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public String generateToken(Map<String, Object> claims) {
        return prefix + " " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            String actualToken = removePrefix(token);
//            parseBuilder() 中的 setSigningKey() 方法用解析 token，這會自動驗證簽名（secret）。
//            如果簽名無效，會拋出 SecurityException。
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(actualToken)
                    .getBody();

            // 驗證是否過期
            if (isTokenExpired(claims)) {
                throw new ApplicationException(ApplicationErrorCode.EXPIRED_JWT_TOKEN);
            }

            return true;
        } catch (SecurityException e) {
            // 簽名驗證失敗
            throw new ApplicationException(ApplicationErrorCode.SECURITY_EXCEPTION);
        } catch (ExpiredJwtException e) {
            // Token 已過期
            throw new ApplicationException(ApplicationErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            // 不支持的 JWT token
            throw new ApplicationException(ApplicationErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            // JWT 字符串為空
            throw new ApplicationException(ApplicationErrorCode.ILLIGAL_ARGUMENT);
        } catch (Exception e) {
            // 其他錯誤
            throw new ApplicationException(ApplicationErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    private String removePrefix(String token) {
        if (token != null && token.startsWith(prefix + " ")) {
            return token.substring((prefix + " ").length());
        }
        return token;
    }

    public void printJwtConfig() {
        System.out.println("webBackendSecret: " + webBackendSecret);
        System.out.println("expiration: " + expiration);
        System.out.println("prefix: " + prefix);
        System.out.println("headerString: " + headerString);
    }
}
