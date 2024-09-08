package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationErrorCode;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ProductApplicationMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCart;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.CartItemRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations.CartItemRepositoryImpl;
import com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations.ProductRepositoryImpl;
import com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations.ShoppingCartRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
                .map(ProductApplicationMapper::covertProductToGetProductCardsDto)
                .toList()).orElseGet(List::of);

    }

    @Transactional
    public void addProductToCart(AddToCartDto addToCartDto) {
        ShoppingCart cart = getOrCreateShoppingCart(addToCartDto.getUserId());
        Product product = findProductById(addToCartDto.getProductId());

        cart.addProduct(product, 1);
        shoppingCartRepository.save(cart);
    }

    private ShoppingCart getOrCreateShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart(userId);
                    return shoppingCartRepository.save(newCart);
                });
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.PRODUCT_NOT_FOUND));
    }

    public void removeProductFromCart(Long userId, Long productId) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        cart.removeProduct(productId);
        shoppingCartRepository.save(cart);
    }

    public BigDecimal getCartTotalPrice(Long userId) {
        ShoppingCart cart = getOrCreateShoppingCart(userId);
        return cart.getTotalPrice();
    }
}
