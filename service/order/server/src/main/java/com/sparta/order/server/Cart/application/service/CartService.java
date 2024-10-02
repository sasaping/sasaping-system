package com.sparta.order.server.Cart.application.service;

import com.sparta.order.server.Cart.domain.model.CartProduct;
import com.sparta.order.server.Cart.domain.model.ProductInfo;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.AddRequest;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  private final HashOperations<String, String, ProductInfo> cartOps;

  public CartService(RedisTemplate<String, CartProduct> cartTemplate) {
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
  }


  public Map<String, CartDto.ProductInfoDto> updateCart(Long userId) {
    String redisKey = createRedisKey(userId);
    Map<String, ProductInfo> cartProductInfos = cartOps.entries(redisKey);
    Map<String, CartDto.ProductInfoDto> response = cartProductInfos.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> CartDto.ProductInfoDto.fromModel(entry.getValue())
        ));
    return response;


  }


  private String createRedisKey(Long userId) {
    return "cart:" + userId.toString();
  }

}
