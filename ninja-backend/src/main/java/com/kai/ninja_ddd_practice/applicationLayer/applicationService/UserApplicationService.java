package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.LoginDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.RegistryDto;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationErrorCode;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.UserApplicationMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.PasswordEncryptionUtil;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncryptionUtil passwordEncryptionUtil;
    private final JwtUtil jwtUtil;

    public UserApplicationService(UserRepository userRepository, PasswordEncryptionUtil passwordEncryptionUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncryptionUtil = passwordEncryptionUtil;
        this.jwtUtil = jwtUtil;
    }

    public String registry(RegistryDto registryDto) {
        if (userRepository.existsByUsername(registryDto.getUsername())) {
            throw new ApplicationException(ApplicationErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByProfile_Email(registryDto.getEmail())) {
            throw new ApplicationException(ApplicationErrorCode.EMAIL_ALREADY_EXISTS);
        }

//        產生隨機鹽值與加密密碼
        String salt = passwordEncryptionUtil.generateSalt();
        String encryptedPassword = passwordEncryptionUtil.encryptPassword(registryDto.getPassword(), salt);

//        建立使用者
        User user = UserApplicationMapper.convertRegistryDtoToUser(registryDto);
        user.getCredentials().setRandomSalt(salt);
        user.getCredentials().setHashedPassword(encryptedPassword);

        userRepository.save(user);

        return "User registered successfully!";
    }

    public String login(LoginDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND));

        if (!passwordEncryptionUtil.verifyPassword(request.getPassword(), user.getCredentials().getRandomSalt(), user.getCredentials().getHashedPassword())) {
            throw new ApplicationException(ApplicationErrorCode.INVALID_PASSWORD);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getProfile().getEmail());

        return jwtUtil.generateToken(claims);

    }

    public User getUserById(String id) {
        return userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND));
    }
}
