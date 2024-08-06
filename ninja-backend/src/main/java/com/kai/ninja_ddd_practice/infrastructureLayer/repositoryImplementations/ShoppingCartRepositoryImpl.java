package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCart;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepositoryImpl extends JpaRepository<ShoppingCart, Long>, ShoppingCartRepository {
}
