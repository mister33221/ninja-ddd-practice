package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductCategoryRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductCategoryEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers.ProductCategoryEntityMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories.ProductCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 產品分類倉儲實作 - 防腐層實作
 */
@Repository
@RequiredArgsConstructor
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    private final ProductCategoryJpaRepository jpaRepository;
    private final ProductCategoryEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategory> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategory> findAllActive() {
        return jpaRepository.findAllActive()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategory> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public ProductCategory save(ProductCategory category) {
        ProductCategoryEntity entity;
        
        if (category.getId() != null && jpaRepository.existsById(category.getId())) {
            // 更新現有分類
            entity = jpaRepository.findById(category.getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            mapper.updateEntity(entity, category);
        } else {
            // 創建新分類
            entity = mapper.toEntity(category);
        }
        
        ProductCategoryEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
