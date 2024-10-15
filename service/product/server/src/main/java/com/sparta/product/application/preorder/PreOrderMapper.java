package com.sparta.product.application.preorder;

import com.sparta.product.domain.model.PreOrder;
import com.sparta.product.presentation.request.PreOrderCreateRequest;
import dto.OrderDto.OrderCreateRequest;
import dto.OrderDto.OrderProductInfo;
import java.util.List;

public class PreOrderMapper {
  public static PreOrder toEntity(PreOrderCreateRequest request) {
    return PreOrder.builder()
        .productId(request.productId())
        .preOrderTitle(request.preOrderTitle())
        .startDateTime(request.startDateTime())
        .endDateTime(request.endDateTime())
        .releaseDateTime(request.releaseDateTime())
        .availableQuantity(request.availableQuantity())
        .build();
  }

  public static OrderCreateRequest toDto(Long preOrderId, Long addressId) {
    OrderProductInfo orderProduct = new OrderProductInfo(preOrderId.toString(), 1, null);
    return new OrderCreateRequest("PREORDER", List.of(orderProduct), null, addressId);
  }
}
