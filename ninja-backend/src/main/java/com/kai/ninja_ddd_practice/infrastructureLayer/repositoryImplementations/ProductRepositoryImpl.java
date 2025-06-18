package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers.ProductEntityMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 產品倉儲實作 - 防腐層的核心實作
 * 負責隔離領域層與數據庫技術細節
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductPure> findById(ProductId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPure> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPure> findAllActive() {
        return jpaRepository.findAllActive()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPure> findByCategoryId(Long categoryId) {
        return jpaRepository.findActiveByCategoryId(categoryId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPure> findAvailableProducts() {
        return jpaRepository.findAvailableProducts()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductPure save(ProductPure product) {
        ProductEntity entity;
        
        if (product.getId() != null && existsById(product.getId())) {
            // 更新現有產品
            entity = jpaRepository.findById(product.getId().getValue())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            mapper.updateEntity(entity, product);
        } else {
            // 創建新產品
            entity = mapper.toEntity(product);
        }
        
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(ProductId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ProductId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
