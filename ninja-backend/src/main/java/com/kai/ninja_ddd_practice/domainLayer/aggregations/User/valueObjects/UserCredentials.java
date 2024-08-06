package com.kai.ninja_ddd_practice.domainLayer.aggregations.User.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentials {
    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
}
