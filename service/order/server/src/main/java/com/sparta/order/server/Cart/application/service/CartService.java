package com.sparta.order.server.Cart.application.service;

import com.sparta.order.server.Cart.domain.model.CartProduct;
import com.sparta.order.server.Cart.domain.model.ProductInfo;
import com.sparta.order.server.Cart.presentation.request.CartRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  private final HashOperations<String, String, ProductInfo> cartOps;

  public CartService(RedisTemplate<String, CartProduct> cartTemplate) {
    this.cartOps = cartTemplate.opsForHash();
  }

  public void addCart(CartRequest.Add request) {
    String redisKey = createRedisKey(request.getUserId());
    ProductInfo existingProductInfo = cartOps.get(redisKey, request.getProductId());

    if (existingProductInfo != null) {
      existingProductInfo.addQuantity(request.getProductInfoRequest().getQuantity());
      cartOps.put(redisKey, request.getProductId(),
          existingProductInfo);
    } else {
      cartOps.put(redisKey, request.getProductId(),
          request.getProductInfoRequest().toEntity());
    }
  }

  private String createRedisKey(Long userId) {
    return "cart:" + userId.toString();
  }

}
