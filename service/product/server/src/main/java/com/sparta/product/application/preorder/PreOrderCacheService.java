package com.sparta.product.application.preorder;

import com.sparta.product.domain.model.PreOrder;
import com.sparta.product.infrastructure.utils.PreOrderRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreOrderCacheService {
  private final PreOrderService preOrderService;

  @Cacheable(cacheNames = "preOrder", key = "#preOrderId")
  public PreOrderRedisDto getPreOrderCache(Long preOrderId) {
    PreOrder preOrder = preOrderService.findPreOrderByPreOrderId(preOrderId);
    return new PreOrderRedisDto(preOrder);
  }
}
