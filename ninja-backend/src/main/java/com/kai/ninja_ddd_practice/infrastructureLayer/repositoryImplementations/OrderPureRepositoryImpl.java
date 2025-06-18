package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.aggregateRoot.OrderPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.OrderId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.OrderStatus;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.OrderPureRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.OrderEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers.OrderEntityMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 訂單防腐層 Repository 實作
 * 負責領域模型與基礎設施層的隔離轉換
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderPureRepositoryImpl implements OrderPureRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderPure> findById(OrderId id) {
        log.debug("Finding order by id: {}", id.getValue());
        
        return jpaRepository.findByIdWithItems(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findByUserId(UserId userId) {
        log.debug("Finding orders by userId: {}", userId.getValue());
        
        List<OrderEntity> entities = jpaRepository.findByUserIdWithItems(userId.getValue());
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findByStatus(OrderStatus status) {
        log.debug("Finding orders by status: {}", status);
        
        OrderEntity.OrderStatusEnum entityStatus = mapToEntityStatus(status);
        List<OrderEntity> entities = jpaRepository.findByStatusWithItems(entityStatus);
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findByUserIdAndStatus(UserId userId, OrderStatus status) {
        log.debug("Finding orders by userId: {} and status: {}", userId.getValue(), status);
        
        OrderEntity.OrderStatusEnum entityStatus = mapToEntityStatus(status);
        List<OrderEntity> entities = jpaRepository.findByUserIdAndStatus(userId.getValue(), entityStatus);
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional
    public OrderPure save(OrderPure order) {
        log.debug("Saving order for user: {}", order.getUserId().getValue());
        
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(OrderId id) {
        log.debug("Deleting order by id: {}", id.getValue());
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(OrderId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findAll() {
        log.debug("Finding all orders");
        
        List<OrderEntity> entities = jpaRepository.findAllWithItems();
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding orders between: {} and {}", startTime, endTime);
        
        List<OrderEntity> entities = jpaRepository.findByCreatedAtBetween(startTime, endTime);
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findByUserIdAndCreatedAtBetween(UserId userId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding orders by userId: {} between: {} and {}", userId.getValue(), startTime, endTime);
        
        List<OrderEntity> entities = jpaRepository.findByUserIdAndCreatedAtBetween(
                userId.getValue(), startTime, endTime);
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderPure> findPendingOrdersOlderThan(LocalDateTime cutoffTime) {
        log.debug("Finding pending orders older than: {}", cutoffTime);
        
        List<OrderEntity> entities = jpaRepository.findByStatusAndCreatedAtBefore(
                OrderEntity.OrderStatusEnum.PENDING, cutoffTime);
        return mapper.toDomainList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserId(UserId userId) {
        return jpaRepository.countByUserId(userId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(OrderStatus status) {
        OrderEntity.OrderStatusEnum entityStatus = mapToEntityStatus(status);
        return jpaRepository.countByStatus(entityStatus);
    }

    /**
     * 領域狀態 -> Entity 狀態
     */
    private OrderEntity.OrderStatusEnum mapToEntityStatus(OrderStatus domainStatus) {
        return switch (domainStatus) {
            case PENDING -> OrderEntity.OrderStatusEnum.PENDING;
            case PAID -> OrderEntity.OrderStatusEnum.PAID;
            case PROCESSING -> OrderEntity.OrderStatusEnum.PROCESSING;
            case SHIPPED -> OrderEntity.OrderStatusEnum.SHIPPED;
            case COMPLETED -> OrderEntity.OrderStatusEnum.COMPLETED;
            case CANCELLED -> OrderEntity.OrderStatusEnum.CANCELLED;
        };
    }
}
