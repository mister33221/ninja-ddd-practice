package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationErrorCode;
import com.kai.ninja_ddd_practice.applicationLayer.exception.ApplicationException;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ShoppingCartApplicationLayerMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCart;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.User;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.CartItemRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.UserRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartApplicationService {

    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ShoppingCartApplicationService(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
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

    @Transactional
    public GetShoppingCartDto getShoppingCart( String token) {
        Long userId = jwtUtil.extractUserId(token);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.SHOPPING_CART_NOT_FOUND));


        return ShoppingCartApplicationLayerMapper.covertShoppingCartToGetShoppingCartDto(cart);
    }

    @Transactional
    public void updateCartItemQuantity(UpdateCartItemQuantityDto updateCartItemQuantityDto) {
        ShoppingCart cart = shoppingCartRepository.findById(updateCartItemQuantityDto.getCartId())
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.SHOPPING_CART_NOT_FOUND));

        CartItem cartItem = cart.getCartItemById(updateCartItemQuantityDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.updateQuantity(updateCartItemQuantityDto.getQuantity());
        shoppingCartRepository.save(cart);
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
