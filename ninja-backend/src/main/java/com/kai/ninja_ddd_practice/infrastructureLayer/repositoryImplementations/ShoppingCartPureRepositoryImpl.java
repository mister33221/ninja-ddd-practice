package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.ShoppingCartId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartPureRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ShoppingCartEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers.ShoppingCartEntityMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories.ShoppingCartJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 購物車防腐層 Repository 實作
 * 負責領域模型與基礎設施層的隔離轉換
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartPureRepositoryImpl implements ShoppingCartPureRepository {

    private final ShoppingCartJpaRepository jpaRepository;
    private final ShoppingCartEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCartPure> findById(ShoppingCartId id) {
        log.debug("Finding shopping cart by id: {}", id.getValue());
        
        return jpaRepository.findByIdWithItems(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCartPure> findByUserId(UserId userId) {
        log.debug("Finding shopping cart by userId: {}", userId.getValue());
        
        return jpaRepository.findByUserIdWithItems(userId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public ShoppingCartPure save(ShoppingCartPure shoppingCart) {
        log.debug("Saving shopping cart for user: {}", shoppingCart.getUserId().getValue());
        
        ShoppingCartEntity entity = mapper.toEntity(shoppingCart);
        ShoppingCartEntity savedEntity = jpaRepository.save(entity);
        
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(ShoppingCartId id) {
        log.debug("Deleting shopping cart by id: {}", id.getValue());
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional
    public void deleteByUserId(UserId userId) {
        log.debug("Deleting shopping cart by userId: {}", userId.getValue());
        jpaRepository.deleteByUserId(userId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ShoppingCartId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(UserId userId) {
        return jpaRepository.existsByUserId(userId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartPure> findAll() {
        log.debug("Finding all shopping carts");
        
        List<ShoppingCartEntity> entities = jpaRepository.findAllWithItems();
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartPure> findByUserIds(List<UserId> userIds) {
        log.debug("Finding shopping carts by userIds: {}", userIds);
        
        List<Long> userIdValues = userIds.stream()
                .map(UserId::getValue)
                .toList();
                
        List<ShoppingCartEntity> entities = jpaRepository.findByUserIdIn(userIdValues);
        return mapper.toDomainList(entities);
    }
}
