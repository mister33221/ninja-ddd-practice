package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationErrorCode;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.UserMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.UserRepository;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.RegistryRequest;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registry(RegistryRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApplicationException(ApplicationErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByProfile_Email(request.getEmail())) {
            throw new ApplicationException(ApplicationErrorCode.EMAIL_ALREADY_EXISTS);
        }
//        建立使用者
        User user = UserMapper.convertRegistryRequestToUser(request);
        userRepository.save(user);

        return "User registered successfully!";
    }
}
