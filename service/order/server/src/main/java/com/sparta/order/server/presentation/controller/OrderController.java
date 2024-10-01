package com.sparta.order.server.presentation.controller;

import com.sparta.order.server.application.service.OrderService;
import com.sparta.order.server.presentation.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public void createOrder(Long userId, @RequestBody OrderCreateRequest request) {
    orderService.createOrder(userId, request);

  }

}
