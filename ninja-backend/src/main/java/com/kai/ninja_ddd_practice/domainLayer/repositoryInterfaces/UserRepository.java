package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByProfile_Email(String email);

    Optional<User> findByUsername(String username);
}
