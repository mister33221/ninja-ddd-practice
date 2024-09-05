package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductStatus;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductCategoryRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductApplicationService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;


    public ProductApplicationService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

public List<GetProductCardsDto> getProductCards() {
    List<Product> products = productRepository.findByStatus(ProductStatus.PULL_ON_SHELVES.getStatus());
    if (products == null) {
        products = Collections.emptyList();
    }

    return products.stream().map(product -> {
        String categoryName = productCategoryRepository.findById(product.getCategoryId())
                .map(category -> category.getName())
                .orElse("Unknown Category");

        return GetProductCardsDto.builder()
                .id(product.getId())
                .name(product.getDetails().getName())
                .description(product.getDetails().getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .category(categoryName)
                .build();
    }).toList();
}
}
