package com.sparta.order.server.Cart.application.service;

import com.sparta.order.server.Cart.exception.CartErrorCode;
import com.sparta.order.server.Cart.exception.CartException;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.Request;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

  // TODO 상품 단건조회 API 구현 이후 상품 존재여부 검증 로직 추가
  // TODO 상품 정보 조회 API 구현 이후 상품 정보 추가 로직 추가

  private final RedisTemplate<String, Map<String, Integer>> cartTemplate;
  private final HashOperations<String, String, Integer> cartOps;
  private static final long CART_EXPIRE_TIME = 30 * 24 * 60 * 60;

  public CartService(RedisTemplate<String, Map<String, Integer>> cartTemplate) {
    this.cartTemplate = cartTemplate;
    this.cartOps = cartTemplate.opsForHash();
  }

  @Transactional
  public void addCart(Request request) {
    String redisKey = createRedisKey(request.getUserId());
    Integer existingProductQuantity = cartOps.get(redisKey, request.getProductId());

    if (existingProductQuantity != null) {
      existingProductQuantity += request.getQuantity();
      cartOps.put(redisKey, request.getProductId(), existingProductQuantity);
    } else {
      cartOps.put(redisKey, request.getProductId(), request.getQuantity());
    }
    cartTemplate.expire(redisKey, CART_EXPIRE_TIME, TimeUnit.SECONDS);
  }

  public Map<String, Integer> getCart(Long userId) {
    String redisKey = createRedisKey(userId);
    validateUserCartExists(redisKey);
    Map<String, Integer> response = cartOps.entries(redisKey);
    // TODO CartProduct 객체로 변환 후 정보 추가하여 반환
    return response;
  }

  @Transactional
  public void updateCart(CartDto.Request request) {
    String redisKey = createRedisKey(request.getUserId());
    validateUserCartExists(redisKey);
    Integer existingProductQuantity = cartOps.get(redisKey, request.getProductId());

    if (existingProductQuantity != null) {
      cartOps.put(redisKey, request.getProductId(), request.getQuantity());
      cartTemplate.expire(redisKey, CART_EXPIRE_TIME, TimeUnit.SECONDS);
    } else {
      throw new CartException(CartErrorCode.PRODUCT_NOT_IN_CART);
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

  private String createRedisKey(Long userId) {
    return "cart:" + userId.toString();
  }

  private void validateUserCartExists(String redisKey) {
    if (cartOps.entries(redisKey).isEmpty()) {
      throw new CartException(CartErrorCode.CART_NOT_FOUND);
    }
  }

}
