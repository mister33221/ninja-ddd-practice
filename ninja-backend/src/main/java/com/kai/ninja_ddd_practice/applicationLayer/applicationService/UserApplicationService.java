package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

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
//        判斷是否有重複的使用者名稱、email
        if (userRepository.existsByUsername(request.getUsername())) {
//            TODO: 這裡應該要改成自訂例外
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByProfile_Email(request.getEmail())) {
//            TODO: 這裡應該要改成自訂例外
            throw new RuntimeException("Email is already in use!");
        }
//        建立使用者
        User user = UserMapper.convertRegistryRequestToUser(request);
        userRepository.save(user);

        return "User registered successfully!";
    }
}
