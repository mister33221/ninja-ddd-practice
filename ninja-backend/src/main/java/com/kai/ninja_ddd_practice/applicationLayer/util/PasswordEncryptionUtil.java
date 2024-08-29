package com.kai.ninja_ddd_practice.applicationLayer.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordEncryptionUtil {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordEncryptionUtil() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String encryptPassword(String password, String salt) {
        return bCryptPasswordEncoder.encode(password + salt);
    }

    public boolean verifyPassword(String rawPassword, String salt, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword + salt, encodedPassword);
    }
}
