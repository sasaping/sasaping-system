package com.sparta.product.application.preorder;

import static com.sparta.product.infrastructure.utils.RedisUtils.getRedisKeyOfPreOrder;

import com.sparta.product.domain.repository.redis.RedisRepository;
import com.sparta.product.infrastructure.utils.PreOrderRedisDto;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreOrderRedisService {
  private final RedisRepository redisRepository;

  public void validateQuantity(PreOrderRedisDto cache, long userId) {
    if (!availableUser(cache.preOrderId(), userId))
      throw new ProductServerException(ProductErrorCode.ALREADY_PREORDER);
    if (!availableQuantity(cache.availableQuantity(), cache.preOrderId()))
      throw new ProductServerException(ProductErrorCode.EXCEED_PREORDER_QUANTITY);
  }

  public void preOrder(String key, long userId) {
    redisRepository.sAdd(key, Long.toString(userId));
  }

  public boolean availableUser(long preOrderId, long userId) { // 중복 요청 확인
    String key = getRedisKeyOfPreOrder(preOrderId);
    return !redisRepository.sIsMember(key, String.valueOf(userId));
  }

  public boolean availableQuantity(int availableQuantity, long preOrderId) { // 수량 검증
    String key = getRedisKeyOfPreOrder(preOrderId);
    return availableQuantity > redisRepository.sCard(key);
  }
}
