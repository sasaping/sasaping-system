package com.sparta.product.infrastructure.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.product.domain.model.PreOrder;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import java.time.LocalDateTime;

public record PreOrderRedisDto(
    Long preOrderId,
    Integer availableQuantity,
    @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime startDateTime,
    @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime endDateTime) {
  public PreOrderRedisDto(PreOrder preOrder) {
    this(
        preOrder.getPreOrderId(),
        preOrder.getAvailableQuantity(),
        preOrder.getStartDateTime(),
        preOrder.getEndDateTime());
  }

  private boolean isReservation() {
    LocalDateTime now = LocalDateTime.now();
    return startDateTime.isBefore(now) && endDateTime.isAfter(now);
  }

  public void validateReservationDate() {
    if (!isReservation())
      throw new ProductServerException(ProductErrorCode.INVALID_PREORDER_DATETIME);
  }
}
