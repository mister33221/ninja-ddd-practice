package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;


import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.InventoryItem;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.InventoryItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepositoryImpl extends JpaRepository<InventoryItem, Long>, InventoryItemRepository {
}
