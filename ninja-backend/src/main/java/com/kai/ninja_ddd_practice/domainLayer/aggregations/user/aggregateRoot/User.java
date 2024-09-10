package com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateUserInfoDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserCredentials;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Embedded
    private UserProfile profile;

    @Embedded
    private UserCredentials credentials;

    public void updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        this.username = updateUserInfoDto.getUsername();
        this.profile.setFullName(updateUserInfoDto.getFullName());
        this.profile.setPhoneNumber(updateUserInfoDto.getPhoneNumber());
        this.profile.setAddress(updateUserInfoDto.getAddress());
        this.profile.setDateOfBirth(LocalDate.parse(updateUserInfoDto.getDateOfBirth()));
    }
}
