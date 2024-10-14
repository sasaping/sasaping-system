package com.sparta.product.application.preorder;

import com.sparta.product.domain.model.PreOrder;
import com.sparta.product.presentation.request.PreOrderCreateRequest;

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
}
