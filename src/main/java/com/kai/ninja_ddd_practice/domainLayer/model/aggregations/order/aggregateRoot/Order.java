package com.kai.ninja_ddd_practice.domainLayer.model.aggregations.order.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.model.enums.OrderStatus;
import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.order.valueObject.OrderItem;
import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.order.valueObject.PaymentInfo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @Embedded
    @Enumerated(EnumType.STRING) // 指定枚舉類型的映射策略。這裡使用的是字符串形式。表示雖然我在這邊的型別是枚舉類型，但在 DB 中，它將被映射為字符串形式。
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Embedded
    private PaymentInfo paymentInfo;

    public void addItem(Long productId, int quantity, BigDecimal price) {  }
    public void updateStatus(OrderStatus newStatus) {  }
    public void processPayment(PaymentInfo paymentInfo) {  }
}
