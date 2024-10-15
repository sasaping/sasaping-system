package com.sparta.order.server.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.application.service.OrderCreateService;
import com.sparta.order.server.application.service.OrderService;
import com.sparta.order.server.presentation.dto.OrderDto.OrderCreateRequest;
import com.sparta.order.server.presentation.dto.OrderDto.OrderGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderCreateService orderCreateService;
  private final OrderService orderService;

  @PostMapping
  public ApiResponse<Long> createOrder(@AuthenticationPrincipal JwtClaim userClaim,
      @RequestBody OrderCreateRequest request) {
    return ApiResponse.created(orderCreateService.createOrder(userClaim.getUserId(), request));
  }

  @PatchMapping("/{orderId}/cancel")
  public ApiResponse<Long> cancelOrder(@AuthenticationPrincipal JwtClaim userClaim,
      @PathVariable(name = "orderId") Long orderId) {
    return ApiResponse.ok(orderService.cancelOrder(userClaim.getUserId(), orderId));
  }

  @GetMapping("/{orderId}")
  public ApiResponse<OrderGetResponse> getOrder(@AuthenticationPrincipal JwtClaim userClaim,
      @PathVariable(name = "orderId") Long orderId) {
    return ApiResponse.ok(orderService.getOrder(userClaim.getUserId(), orderId));
  }

}
