package com.sparta.product.application.preorder;

import com.sparta.common.domain.entity.KafkaTopicConstant;
import com.sparta.product.domain.model.PreOrder;
import com.sparta.product.domain.model.PreOrderState;
import com.sparta.product.infrastructure.messaging.PreOrderProducer;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import dto.OrderDto.OrderCreateRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PreOrderFacadeService {
  private final PreOrderService preOrderService;
  private final PreOrderProducer preOrderProducer;
  private final PreOrderLockService preOrderLockService;

  @Transactional
  public void preOrder(Long preOrderId, Long addressId, Long userId) {
    PreOrder preOrder = preOrderService.findPreOrderByPreOrderId(preOrderId);
    preOrderLockService.reservation(preOrderId, userId);
    OrderCreateRequest createRequest = PreOrderMapper.toDto(preOrderId, addressId);
    preOrderProducer.send(KafkaTopicConstant.PROCESS_PREORDER, preOrderId, createRequest);
  }

  private void validatePreOrder(PreOrder preOrder) {
    if (preOrder.getState() != PreOrderState.OPEN_FOR_ORDER)
      throw new ProductServerException(ProductErrorCode.NOT_OPEN_FOR_PREORDER);

    LocalDateTime now = LocalDateTime.now();
    if (now.isBefore(preOrder.getStartDateTime()) || now.isAfter(preOrder.getEndDateTime())) {
      throw new ProductServerException(ProductErrorCode.CLOSED_PREORDER);
    }
  }
}
