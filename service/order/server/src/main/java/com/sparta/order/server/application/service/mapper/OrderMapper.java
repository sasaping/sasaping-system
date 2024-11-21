package com.sparta.order.server.application.service.mapper;

import com.sparta.order.server.domain.model.Order;
import dto.NotificationOrderDto;

public class OrderMapper {

  public static NotificationOrderDto toNotificationOrderDto(Order order,
      String displayProductName) {
    return new NotificationOrderDto(order.getOrderId(), order.getUserId(),
        order.getState().getDescription(), displayProductName, order.getTotalQuantity());
  }

}
