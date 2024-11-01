package com.sparta.product.application.preorder;

import com.sparta.common.domain.entity.KafkaTopicConstant;
import com.sparta.product.infrastructure.messaging.PreOrderProducer;
import com.sparta.product.infrastructure.utils.PreOrderRedisDto;
import dto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PreOrderFacadeService {
  private final PreOrderProducer preOrderProducer;
  private final PreOrderLockService preOrderLockService;

  @Transactional
  public void preOrder(Long preOrderId, Long addressId, Long userId) {
    PreOrderRedisDto cachedData = preOrderLockService.reservation(preOrderId, userId);
    OrderCreateRequest createRequest = PreOrderMapper.toDto(cachedData.productId(), addressId);
    preOrderProducer.send(
        KafkaTopicConstant.PROCESS_PREORDER, Long.toString(userId), createRequest);
  }
}
