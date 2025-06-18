package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.aggregateRoot.OrderPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.*;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.OrderEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 訂單領域模型與 JPA Entity 轉換器
 */
@Component
public class OrderEntityMapper {

    /**
     * Entity -> 領域模型
     */
    public OrderPure toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        List<OrderItemPure> items = entity.getItems().stream()
                .map(this::orderItemToDomain)
                .toList();        PaymentInfoPure paymentInfo = null;
        if (entity.getPaymentMethod() != null) {
            paymentInfo = PaymentInfoPure.builder()
                    .paymentMethod(entity.getPaymentMethod())
                    .amount(entity.getPaymentAmount() != null ? 
                        Money.of(entity.getPaymentAmount()) : null)
                    .status(entity.getPaymentStatus())
                    .build();
        }

        return OrderPure.builder()
                .id(entity.getId() != null ? OrderId.of(entity.getId()) : null)
                .userId(UserId.of(entity.getUserId()))
                .items(items)
                .status(mapToDomainStatus(entity.getStatus()))
                .totalAmount(entity.getTotalAmount())
                .paymentInfo(paymentInfo)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * 領域模型 -> Entity
     */
    public OrderEntity toEntity(OrderPure domain) {
        if (domain == null) {
            return null;
        }

        OrderEntity entity = OrderEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .userId(domain.getUserId().getValue())
                .status(mapToEntityStatus(domain.getStatus()))
                .totalAmount(domain.getTotalAmount())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();        // 設置付款資訊
        if (domain.getPaymentInfo() != null) {
            entity.setPaymentMethod(domain.getPaymentInfo().getPaymentMethod());
            entity.setPaymentAmount(domain.getPaymentInfo().getAmount().getAmount());
            entity.setPaymentStatus(domain.getPaymentInfo().getStatus());
        }

        List<OrderItemEntity> items = domain.getItems().stream()
                .map(item -> orderItemToEntity(item, entity.getId()))
                .toList();
        
        entity.setItems(items);
        return entity;
    }

    /**
     * OrderItemEntity -> OrderItemPure
     */
    private OrderItemPure orderItemToDomain(OrderItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return OrderItemPure.builder()
                .id(entity.getId() != null ? OrderItemId.of(entity.getId()) : null)
                .productId(ProductId.of(entity.getProductId()))
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }

    /**
     * OrderItemPure -> OrderItemEntity
     */
    private OrderItemEntity orderItemToEntity(OrderItemPure domain, Long orderId) {
        if (domain == null) {
            return null;
        }

        return OrderItemEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .orderId(orderId)
                .productId(domain.getProductId().getValue())
                .productName(domain.getProductName())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .build();
    }

    /**
     * Entity 狀態 -> 領域狀態
     */
    private OrderStatus mapToDomainStatus(OrderEntity.OrderStatusEnum entityStatus) {
        if (entityStatus == null) {
            return OrderStatus.PENDING;
        }
        return switch (entityStatus) {
            case PENDING -> OrderStatus.PENDING;
            case PAID -> OrderStatus.PAID;
            case PROCESSING -> OrderStatus.PROCESSING;
            case SHIPPED -> OrderStatus.SHIPPED;
            case COMPLETED -> OrderStatus.COMPLETED;
            case CANCELLED -> OrderStatus.CANCELLED;
        };
    }

    /**
     * 領域狀態 -> Entity 狀態
     */
    private OrderEntity.OrderStatusEnum mapToEntityStatus(OrderStatus domainStatus) {
        if (domainStatus == null) {
            return OrderEntity.OrderStatusEnum.PENDING;
        }
        return switch (domainStatus) {
            case PENDING -> OrderEntity.OrderStatusEnum.PENDING;
            case PAID -> OrderEntity.OrderStatusEnum.PAID;
            case PROCESSING -> OrderEntity.OrderStatusEnum.PROCESSING;
            case SHIPPED -> OrderEntity.OrderStatusEnum.SHIPPED;
            case COMPLETED -> OrderEntity.OrderStatusEnum.COMPLETED;
            case CANCELLED -> OrderEntity.OrderStatusEnum.CANCELLED;
        };
    }

    /**
     * 批量轉換：Entity List -> Domain List
     */
    public List<OrderPure> toDomainList(List<OrderEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * 批量轉換：Domain List -> Entity List
     */
    public List<OrderEntity> toEntityList(List<OrderPure> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .toList();
    }
}
