package com.sparta.product.application.preorder;

import static com.sparta.product.infrastructure.utils.RedisUtils.getRedisKeyOfPreOrder;

import com.sparta.product.infrastructure.utils.PreOrderRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreOrderLockService {
  private final PreOrderRedisService redisService;
  private final PreOrderCacheService cacheService;
  private final DistributedLockComponent lockComponent;

  public void reservation(long preOrderId, long userId) {
    PreOrderRedisDto cachedPreOrder = cacheService.getPreOrderCache(preOrderId);
    cachedPreOrder.validateReservationDate(); // 사전예약기간인지 검증
    lockComponent.execute( // 락을 걸고
        "preOrderLock_%s".formatted(preOrderId),
        3000,
        3000,
        () -> {
          redisService.validateQuantity(cachedPreOrder, userId); // 이미 예약에 성공한 유저인지, 수량안에 든 유저인지 검증
        });
    redisService.preOrder(getRedisKeyOfPreOrder(preOrderId), userId);
  }
}
