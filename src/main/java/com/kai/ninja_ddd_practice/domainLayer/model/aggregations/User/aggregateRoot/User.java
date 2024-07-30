package com.kai.ninja_ddd_practice.domainLayer.model.aggregations.User.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.User.valueObject.UserCredentials;
import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.User.valueObject.UserProfile;
import jakarta.persistence.*;
import lombok.*;

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

    public void register(String username, String password, String email) {  }
    public void login(String username, String password) {  }
    public void updateProfile(UserProfile newProfile) {  }
}
