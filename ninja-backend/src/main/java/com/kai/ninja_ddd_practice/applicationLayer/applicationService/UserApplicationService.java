package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.LoginDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.RegistryDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateUserInfoDto;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationErrorCode;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.UserApplicationLayerMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.UserPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserCredentialsPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartPureRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.UserPureRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.PasswordEncryptionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserApplicationService {

    private final UserPureRepository userPureRepository;
    private final ShoppingCartPureRepository shoppingCartPureRepository;
    private final PasswordEncryptionUtil passwordEncryptionUtil;
    private final JwtUtil jwtUtil;

    public UserApplicationService(UserPureRepository userPureRepository, 
                                ShoppingCartPureRepository shoppingCartPureRepository, 
                                PasswordEncryptionUtil passwordEncryptionUtil, 
                                JwtUtil jwtUtil) {
        this.userPureRepository = userPureRepository;
        this.shoppingCartPureRepository = shoppingCartPureRepository;
        this.passwordEncryptionUtil = passwordEncryptionUtil;
        this.jwtUtil = jwtUtil;
    }

    public String registry(RegistryDto registryDto) {
        if (userPureRepository.existsByUsername(registryDto.getUsername())) {
            throw new ApplicationException(ApplicationErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userPureRepository.existsByEmail(registryDto.getEmail())) {
            throw new ApplicationException(ApplicationErrorCode.EMAIL_ALREADY_EXISTS);
        }

//        1. 產生隨機鹽值與加密密碼
        String salt = passwordEncryptionUtil.generateSalt();
        String encryptedPassword = passwordEncryptionUtil.encryptPassword(registryDto.getPassword(), salt);

//        2. 建立使用者
        UserPure user = UserApplicationLayerMapper.convertRegistryDtoToUser(registryDto);
        
        // 創建新的憑證對象（因為 @Value 是不可變的）
        UserCredentialsPure newCredentials = user.getCredentials().toBuilder()
                .randomSalt(salt)
                .hashedPassword(encryptedPassword)
                .build();
        
        // 創建新的用戶對象
        UserPure userWithCredentials = user.toBuilder()
                .credentials(newCredentials)
                .build();

        UserPure newUser = userPureRepository.save(userWithCredentials);

//       3. 建立購物車
        shoppingCartPureRepository.save(new ShoppingCartPure(null, newUser.getId()));

        return "User registered successfully!";
    }

    public String login(LoginDto request) {
        UserPure user = userPureRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND));

        if (!passwordEncryptionUtil.verifyPassword(request.getPassword(), user.getCredentials().getRandomSalt(), user.getCredentials().getHashedPassword())) {
            throw new ApplicationException(ApplicationErrorCode.INVALID_PASSWORD);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId().getValue());
        claims.put("username", user.getUsername());
        claims.put("email", user.getProfile().getEmail());

        return jwtUtil.generateToken(claims);

    }

    public UserPure getUserById(String id) {
        UserId userId = UserId.of(Long.parseLong(id));
        return userPureRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND));
    }    public void updateUserInfo(UpdateUserInfoDto updateUserInfoDto, UserId userId) {
        UserPure user = userPureRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND));

//        將更新的方法放在領域內，這樣可以確保領域內的邏輯是正確的
        // 轉換日期字串為 LocalDate
        LocalDate dateOfBirth = null;
        if (updateUserInfoDto.getDateOfBirth() != null && !updateUserInfoDto.getDateOfBirth().trim().isEmpty()) {
            try {
                dateOfBirth = LocalDate.parse(updateUserInfoDto.getDateOfBirth());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        UserPure updatedUser = user.updateUserInfo(
                updateUserInfoDto.getUsername(),
                updateUserInfoDto.getFullName(),
                updateUserInfoDto.getPhoneNumber(),
                updateUserInfoDto.getAddress(),
                dateOfBirth
        );

        userPureRepository.save(updatedUser);
    }
}
