package com.sparta.notification.server.infrastructure.client;

import dto.NotificationOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order")
public interface OrderClient {

  @GetMapping("/internal/orders/order-id")
  NotificationOrderDto getOrder(@RequestParam(value = "orderId") Long orderId);

}
