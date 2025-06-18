package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserCredentials;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * 用戶數據庫實體 - 僅用於持久化，包含所有 JPA 註解
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Embedded
    private UserProfile profile;

    @Embedded
    private UserCredentials credentials;
}