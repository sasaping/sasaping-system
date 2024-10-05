package com.sparta.order.server.Cart.application.service;

import com.sparta.order.server.Cart.domain.model.CartProduct;
import com.sparta.order.server.Cart.domain.model.ProductInfo;
import com.sparta.order.server.Cart.exception.CartErrorCode;
import com.sparta.order.server.Cart.exception.CartException;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.AddRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  // TODO 상품 단건조회 API 구현 이후 상품 존재여부 검증 로직 추가
  // TODO Redis 트랜잭션 추가

  private final RedisTemplate<String, CartProduct> cartTemplate;
  private final HashOperations<String, String, ProductInfo> cartOps;
  private static final long CART_EXPIRE_TIME = 30 * 24 * 60 * 60;

  public CartService(RedisTemplate<String, CartProduct> cartTemplate) {
    this.cartTemplate = cartTemplate;
    this.cartOps = cartTemplate.opsForHash();
  }

  public void addCart(AddRequest request) {
    String redisKey = createRedisKey(request.getUserId());
    ProductInfo existingProductInfo = cartOps.get(redisKey, request.getProductId());

    if (existingProductInfo != null) {
      existingProductInfo.addQuantity(request.getProductInfoDto().getQuantity());
      cartOps.put(redisKey, request.getProductId(),
          existingProductInfo);
    } else {
      cartOps.put(redisKey, request.getProductId(),
          request.getProductInfoDto().toEntity());
    }
    cartTemplate.expire(redisKey, CART_EXPIRE_TIME, TimeUnit.SECONDS);
  }

  public Map<String, CartDto.ProductInfoDto> getCart(Long userId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    Map<String, ProductInfo> cartProductInfos = cartOps.entries(redisKey);
    Map<String, CartDto.ProductInfoDto> response = cartProductInfos.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> CartDto.ProductInfoDto.fromModel(entry.getValue())
        ));
    return response;
  }

  public void updateCart(CartDto.UpdateRequest request) {
    String redisKey = createRedisKey(request.getUserId());
    validateUserCartExists(redisKey);
    ProductInfo existingProductInfo = cartOps.get(redisKey, request.getProductId());

    if (existingProductInfo != null) {
      existingProductInfo.updateQuantity(request.getQuantity());
      cartOps.put(redisKey, request.getProductId(), existingProductInfo);
      cartTemplate.expire(redisKey, CART_EXPIRE_TIME, TimeUnit.SECONDS);
    } else {
      throw new CartException(CartErrorCode.PRODUCT_NOT_IN_CART);
    }
  }

  public void deleteCart(Long userId, String productId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    ProductInfo existingProductInfo = cartOps.get(redisKey, productId);

    if (existingProductInfo != null) {
      cartOps.delete(redisKey, productId);
    } else {
      throw new CartException(CartErrorCode.PRODUCT_NOT_IN_CART);
    }
  }

  public void clearCart(Long userId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    cartOps.keys(redisKey).forEach(key -> cartOps.delete(redisKey, key));
  }

  private String createRedisKey(Long userId) {
    return "cart:" + userId.toString();
  }

  private void validateUserCartExists(String redisKey) {
    if (cartOps.entries(redisKey).isEmpty()) {
      throw new CartException(CartErrorCode.CART_NOT_FOUND);
    }
  }

}
