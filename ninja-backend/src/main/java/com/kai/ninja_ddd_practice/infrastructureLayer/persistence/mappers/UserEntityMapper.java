package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.UserPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.*;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * 用戶實體映射器 - 負責領域模型與數據庫實體間的轉換
 */
@Component
public class UserEntityMapper {

    /**
     * 將數據庫實體轉換為純淨的領域模型
     */
    public UserPure toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserPure.builder()
                .id(entity.getId() != null ? UserId.of(entity.getId()) : null)
                .username(entity.getUsername())
                .profile(mapProfileToDomain(entity.getProfile()))
                .credentials(mapCredentialsToDomain(entity.getCredentials()))
                .build();
    }

    /**
     * 將純淨的領域模型轉換為數據庫實體
     */
    public UserEntity toEntity(UserPure domain) {
        if (domain == null) {
            return null;
        }

        return UserEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .username(domain.getUsername())
                .profile(mapProfileToEntity(domain.getProfile()))
                .credentials(mapCredentialsToEntity(domain.getCredentials()))
                .build();
    }

    /**
     * 將領域 UserProfile 轉換為 JPA Embeddable
     */
    private UserProfile mapProfileToEntity(UserProfilePure profilePure) {
        if (profilePure == null) {
            return null;
        }

        return UserProfile.builder()
                .fullName(profilePure.getFullName())
                .email(profilePure.getEmail())
                .phoneNumber(profilePure.getPhoneNumber())
                .dateOfBirth(profilePure.getDateOfBirth())
                .address(profilePure.getAddress())
                .build();
    }

    /**
     * 將 JPA Embeddable 轉換為領域 UserProfile
     */
    private UserProfilePure mapProfileToDomain(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        return UserProfilePure.builder()
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .dateOfBirth(profile.getDateOfBirth())
                .address(profile.getAddress())
                .build();
    }

    /**
     * 將領域 UserCredentials 轉換為 JPA Embeddable
     */
    private UserCredentials mapCredentialsToEntity(UserCredentialsPure credentialsPure) {
        if (credentialsPure == null) {
            return null;
        }

        return UserCredentials.builder()
                .hashedPassword(credentialsPure.getHashedPassword())
                .lastLoginTime(credentialsPure.getLastLoginTime())
                .randomSalt(credentialsPure.getRandomSalt())
                .build();
    }

    /**
     * 將 JPA Embeddable 轉換為領域 UserCredentials
     */
    private UserCredentialsPure mapCredentialsToDomain(UserCredentials credentials) {
        if (credentials == null) {
            return null;
        }

        return UserCredentialsPure.builder()
                .hashedPassword(credentials.getHashedPassword())
                .lastLoginTime(credentials.getLastLoginTime())
                .randomSalt(credentials.getRandomSalt())
                .build();
    }
}