package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.CartItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepositoryImpl extends JpaRepository<CartItem, Long>, CartItemRepository {
}
