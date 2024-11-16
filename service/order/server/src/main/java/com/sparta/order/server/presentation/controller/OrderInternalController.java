package com.sparta.order.server.presentation.controller;

import com.sparta.order.server.application.service.OrderService;
import dto.NotificationOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
public class OrderInternalController {

  private OrderService orderService;

  @GetMapping
  public NotificationOrderDto getOrder(@RequestParam(value = "orderId") Long orderId) {
    return orderService.getNotificationOrder(orderId);
  }

}
