package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ProductApplicationLayerMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations.ProductRepositoryImpl;
import com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations.ShoppingCartRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductApplicationService {

    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public ProductApplicationService(ProductRepositoryImpl productRepositoryImpl, ShoppingCartRepositoryImpl shoppingCartRepositoryImpl) {
        this.productRepository = productRepositoryImpl;
        this.shoppingCartRepository = shoppingCartRepositoryImpl;
    }

    @Transactional
    public List<GetProductCardsDto> getProductCards() {

        Optional<List<Product>> products = productRepository.findByStatus("PULL_ON_SHELVES");

        return products.map(productList -> productList.stream()
                .map(ProductApplicationLayerMapper::covertProductToGetProductCardsDto)
                .toList()).orElseGet(List::of);

    }
}
