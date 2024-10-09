package com.sparta.order.server.application.service;

import com.sparta.common.domain.exception.BusinessException;
import com.sparta.order.server.Cart.presentation.dto.CartDto.CartProductRequest;
import com.sparta.order.server.Cart.presentation.dto.CartDto.CartProductResponse;
import com.sparta.order.server.exception.CartErrorCode;
import com.sparta.order.server.exception.CartException;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.client.ProductClient;
import com.sparta.product_dto.ProductDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

  private final RedisTemplate<String, Map<String, Integer>> cartTemplate;
  private final HashOperations<String, String, Integer> cartOps;
  private final ProductClient productClient;
  private static final long CART_EXPIRE_TIME = 30 * 24 * 60 * 60;

  public CartService(RedisTemplate<String, Map<String, Integer>> cartTemplate,
      ProductClient productClient) {
    this.cartTemplate = cartTemplate;
    this.cartOps = cartTemplate.opsForHash();
    this.productClient = productClient;
  }

  @Transactional
  public void addCart(CartProductRequest cartProductRequest) {
    validateProductExists(cartProductRequest.getProductId());

    String redisKey = createRedisKey(cartProductRequest.getUserId());
    Integer existingProductQuantity = cartOps.get(redisKey,
        cartProductRequest.getProductId());

    if (existingProductQuantity != null) {
      existingProductQuantity += cartProductRequest.getQuantity();
      cartOps.put(redisKey, cartProductRequest.getProductId(), existingProductQuantity);
    } else {
      cartOps.put(redisKey, cartProductRequest.getProductId(),
          cartProductRequest.getQuantity());
    }
    cartTemplate.expire(redisKey, CART_EXPIRE_TIME, TimeUnit.SECONDS);
  }

  public List<CartProductResponse> getCart(Long userId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    Map<String, Integer> productQuantities = cartOps.entries(redisKey);

    List<ProductDto> products = productClient.getProductList(
        productQuantities.keySet().stream().toList());

    List<CartProductResponse> response = products.stream()
        .map(product -> CartProductResponse.from(product,
            productQuantities.get(product.getProductId().toString())))
        .toList();

    return response;
  }

  @Transactional
  public void updateCart(CartProductRequest cartProductRequest) {
    validateProductExists(cartProductRequest.getProductId());

    String redisKey = createRedisKey(cartProductRequest.getUserId());
    validateUserCartExists(redisKey);

    Integer existingProductQuantity = cartOps.get(redisKey,
        cartProductRequest.getProductId());

    if (existingProductQuantity != null) {
      cartOps.put(redisKey, cartProductRequest.getProductId(),
          cartProductRequest.getQuantity());
      cartTemplate.expire(redisKey, CART_EXPIRE_TIME, TimeUnit.SECONDS);
    } else {
      throw new CartException(
          CartErrorCode.PRODUCT_NOT_IN_CART);
    }
  }

  @Transactional
  public void deleteCart(Long userId, String productId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    Integer existingProductQuantity = cartOps.get(redisKey, productId);

    if (existingProductQuantity != null) {
      cartOps.delete(redisKey, productId);
    } else {
      throw new CartException(CartErrorCode.PRODUCT_NOT_IN_CART);
    }
  }

  @Transactional
  public void clearCart(Long userId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    cartOps.keys(redisKey).forEach(key -> cartOps.delete(redisKey, key));
  }

  @Transactional
  public void orderCartProduct(Long userId, Map<String, Integer> productQuantities) {
    String redisKey = createRedisKey(userId);

    try {
      validateUserCartExists(redisKey);
    } catch (BusinessException e) {
      throw new OrderException(OrderErrorCode.CART_ITEM_ONLY_ORDERABLE);
    }

    productQuantities.forEach((productId, quantity) -> {
          Integer existingProductQuantity = cartOps.get(redisKey, productId);
          if (existingProductQuantity == null) {
            throw new OrderException(OrderErrorCode.CART_ITEM_ONLY_ORDERABLE);
          }
          if (!existingProductQuantity.equals(quantity)) {
            throw new OrderException(OrderErrorCode.CART_ITEM_QUANTITY_MISMATCH, productId);
          }
          cartOps.delete(redisKey, productId);
        }
    );
  }

  private String createRedisKey(Long userId) {
    return "cart:" + userId.toString();
  }

  private void validateUserCartExists(String redisKey) {
    if (cartOps.entries(redisKey).isEmpty()) {
      throw new CartException(CartErrorCode.CART_NOT_FOUND);
    }
  }

  private void validateProductExists(String productId) {
    productClient.getProductList(new ArrayList<>(Arrays.asList(productId)));
  }


}
