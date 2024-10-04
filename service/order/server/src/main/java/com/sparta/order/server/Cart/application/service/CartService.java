package com.sparta.order.server.Cart.application.service;

import com.sparta.order.server.Cart.domain.model.CartProduct;
import com.sparta.order.server.Cart.domain.model.ProductInfo;
import com.sparta.order.server.Cart.exception.CartErrorCode;
import com.sparta.order.server.Cart.exception.CartException;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.AddRequest;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  // TODO 상품 단건조회 API 구현 이후 상품 존재여부 검증 로직 추가
  // TODO Redis 트랜잭션 추가

  private final HashOperations<String, String, ProductInfo> userCartOps;
  private final SetOperations<String, String> cartProductOps;

  public CartService(RedisTemplate<String, CartProduct> userCartRedisTemplate,
      StringRedisTemplate cartProductRedisTemplate) {
    this.userCartOps = userCartRedisTemplate.opsForHash();
    this.cartProductOps = cartProductRedisTemplate.opsForSet();
  }

  public void addCart(AddRequest request) {
    String userCartRedisKey = createUserCartRedisKey(request.getUserId().toString());
    ProductInfo existingProductInfo = userCartOps.get(userCartRedisKey, request.getProductId());

    if (existingProductInfo != null) {
      existingProductInfo.addQuantity(request.getProductInfoDto().getQuantity());
      userCartOps.put(userCartRedisKey, request.getProductId(),
          existingProductInfo);
    } else {
      userCartOps.put(userCartRedisKey, request.getProductId(),
          request.getProductInfoDto().toEntity());
      addUserInCartProduct(request.getProductId(), request.getUserId().toString());
    }
  }

  public Map<String, CartDto.ProductInfoDto> getCart(Long userId) {
    String userCartRedisKey = createUserCartRedisKey(userId.toString());
    validateUserCartExists(userCartRedisKey);
    Map<String, ProductInfo> cartProductInfos = userCartOps.entries(userCartRedisKey);
    Map<String, CartDto.ProductInfoDto> response = cartProductInfos.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> CartDto.ProductInfoDto.fromModel(entry.getValue())
        ));
    return response;
  }

  public void updateCart(CartDto.UpdateRequest request) {
    String userCartRedisKey = createUserCartRedisKey(request.getUserId().toString());
    validateUserCartExists(userCartRedisKey);
    ProductInfo existingProductInfo = userCartOps.get(userCartRedisKey, request.getProductId());

    if (existingProductInfo != null) {
      existingProductInfo.updateQuantity(request.getQuantity());
      userCartOps.put(userCartRedisKey, request.getProductId(), existingProductInfo);
    } else {
      throw new CartException(CartErrorCode.PRODUCT_NOT_IN_CART);
    }
  }

  public void deleteCart(Long userId, String productId) {
    String userCartRedisKey = createUserCartRedisKey(userId.toString());
    validateUserCartExists(userCartRedisKey);
    ProductInfo existingProductInfo = userCartOps.get(userCartRedisKey, productId);

    if (existingProductInfo != null) {
      userCartOps.delete(userCartRedisKey, productId);
    } else {
      throw new CartException(CartErrorCode.PRODUCT_NOT_IN_CART);
    }
    deleteUserInCartProduct(productId, userId.toString());
  }

  public void updateCartInfo(String productId) {
    String cartProductRedisKey = createCartProductRedisKey(productId);
    Set<String> userIds = cartProductOps.members(cartProductRedisKey);

    if (userIds != null && !userIds.isEmpty()) {
      // TODO 변경된 product 단건 조회 API 호출
      for (String userId : userIds) {
        String userCartRedisKey = createUserCartRedisKey(userId);
        // 임시 정보 생성
        ProductInfo existingProductInfo = userCartOps.get(userCartRedisKey, productId);
        existingProductInfo.updateQuantity(1000000);
        
        userCartOps.put(userCartRedisKey, productId, existingProductInfo);
      }
    }

  }

  public void clearCart(Long userId) {
    String userCartRedisKey = createUserCartRedisKey(userId.toString());
    validateUserCartExists(userCartRedisKey);

    Set<String> productIds = userCartOps.keys(userCartRedisKey);
    productIds.forEach(key -> userCartOps.delete(userCartRedisKey, key));
    productIds.forEach(key -> deleteUserInCartProduct(key, userId.toString()));
  }

  private void addUserInCartProduct(String productId, String userId) {
    String cartProductRedisKey = createCartProductRedisKey(productId);
    cartProductOps.add(cartProductRedisKey, userId);
  }

  private void deleteUserInCartProduct(String productId, String userId) {
    String cartProductRedisKey = createCartProductRedisKey(productId);
    cartProductOps.remove(cartProductRedisKey, userId);
  }

  private void validateUserCartExists(String userCartRedisKey) {
    if (userCartOps.entries(userCartRedisKey).isEmpty()) {
      throw new CartException(CartErrorCode.CART_NOT_FOUND);
    }
  }

  private String createUserCartRedisKey(String userId) {
    return "cart:" + userId;
  }

  private String createCartProductRedisKey(String productId) {
    return "cart:product:" + productId + ":users";
  }


}
