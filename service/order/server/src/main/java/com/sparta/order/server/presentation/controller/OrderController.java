package com.sparta.order.server.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.application.service.OrderService;
import com.sparta.order.server.presentation.dto.OrderDto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/{userId}")
  public ApiResponse<Long> createOrder(@PathVariable(name = "userId") Long userId,
      @RequestBody OrderCreateRequest request) {
    return ApiResponse.created(orderService.createOrder(userId, request));
  }

}
