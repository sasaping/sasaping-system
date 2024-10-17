package com.sparta.product.application.preorder;

import com.sparta.product.domain.model.PreOrder;
import com.sparta.product.domain.model.PreOrderState;
import com.sparta.product.infrastructure.utils.PreOrderRedisDto;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
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
    validatePreOrder(preOrder);
    return new PreOrderRedisDto(preOrder);
  }

  private void validatePreOrder(PreOrder preOrder) {
    if (preOrder.getState() != PreOrderState.OPEN_FOR_ORDER)
      throw new ProductServerException(ProductErrorCode.NOT_OPEN_FOR_PREORDER);
  }
}
