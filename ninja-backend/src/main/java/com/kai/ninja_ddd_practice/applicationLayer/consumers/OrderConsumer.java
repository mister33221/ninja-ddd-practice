package com.kai.ninja_ddd_practice.applicationLayer.consumers;


import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private OrderRepository orderRepository;

    public OrderConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "order-created", groupId = "order-group")
    public void consumeOrder(String order) {
        System.out.println("Consumed order: " + order);
    }
}
