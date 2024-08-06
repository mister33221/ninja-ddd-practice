package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.aggregateRoot.Order;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 其實不寫也可以，但是為了程式碼的明確性，我們還是加上
public interface OrderRepositoryImpl extends JpaRepository<Order, Long>, OrderRepository {
}
