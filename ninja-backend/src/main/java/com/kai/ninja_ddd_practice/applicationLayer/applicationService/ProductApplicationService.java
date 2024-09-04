package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductStatus;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetProductsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductApplicationService {

    private final ProductRepository productRepository;


    public ProductApplicationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductList() {
        return productRepository.findByStatus(ProductStatus.PULL_ON_SHELVES.getStatus());
    }
}
