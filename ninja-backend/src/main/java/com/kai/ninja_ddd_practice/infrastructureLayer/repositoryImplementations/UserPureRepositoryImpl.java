package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.UserPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.UserPureRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.UserEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers.UserEntityMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用戶防腐層 Repository 實作
 * 負責協調 JPA Repository 和領域模型之間的轉換
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserPureRepositoryImpl implements UserPureRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<UserPure> findById(UserId id) {
        log.debug("Finding user by id: {}", id.getValue());
        
        return userJpaRepository.findById(id.getValue())
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserPure> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return userJpaRepository.findByUsername(username.trim())
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserPure> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return userJpaRepository.findByProfileEmail(email.trim().toLowerCase())
                .map(userEntityMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking if username exists: {}", username);
        
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return userJpaRepository.existsByUsername(username.trim());
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);
        
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return userJpaRepository.existsByProfileEmail(email.trim().toLowerCase());
    }

    @Override
    public UserPure save(UserPure user) {
        log.debug("Saving user: {}", user.getUsername());
        
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        try {
            UserEntity entity = userEntityMapper.toEntity(user);
            UserEntity savedEntity = userJpaRepository.save(entity);
            UserPure savedUser = userEntityMapper.toDomain(savedEntity);
            
            log.info("Successfully saved user with id: {}", savedUser.getId().getValue());
            return savedUser;
            
        } catch (Exception e) {
            log.error("Failed to save user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void deleteById(UserId id) {
        log.debug("Deleting user by id: {}", id.getValue());
        
        if (!userJpaRepository.existsById(id.getValue())) {
            log.warn("User with id {} does not exist", id.getValue());
            return;
        }
        
        try {
            userJpaRepository.deleteById(id.getValue());
            log.info("Successfully deleted user with id: {}", id.getValue());
            
        } catch (Exception e) {
            log.error("Failed to delete user with id: {}", id.getValue(), e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public List<UserPure> findAllActive() {
        log.debug("Finding all active users");
        
        return userJpaRepository.findAllActiveUsers()
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserPure> findRecentlyLoggedIn(int days) {
        log.debug("Finding users who logged in within {} days", days);
        
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
        
        return userJpaRepository.findRecentlyLoggedInUsers(cutoffTime)
                .stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}