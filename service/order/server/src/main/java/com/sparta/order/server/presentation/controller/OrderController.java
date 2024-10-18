package com.sparta.order.server.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.application.service.OrderCreateService;
import com.sparta.order.server.application.service.OrderService;
import com.sparta.order.server.presentation.dto.OrderDto.MyOrderGetResponse;
import com.sparta.order.server.presentation.dto.OrderDto.OrderGetResponse;
import dto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PatchMapping("/{orderId}")
  public ApiResponse<Long> updateOrderAddress(@AuthenticationPrincipal JwtClaim userClaim,
      @PathVariable(name = "orderId") Long orderId,
      @RequestParam(name = "address_id") Long addressId) {
    return ApiResponse.ok(
        orderService.updateOrderAddress(userClaim.getUserId(), orderId, addressId));
  }

  @GetMapping("/{orderId}")
  public ApiResponse<OrderGetResponse> getOrder(@AuthenticationPrincipal JwtClaim userClaim,
      @PathVariable(name = "orderId") Long orderId) {
    return ApiResponse.ok(orderService.getOrder(userClaim.getUserId(), orderId));
  }

  @GetMapping("/me")
  public ApiResponse<Page<MyOrderGetResponse>> getMyOrder(
      @AuthenticationPrincipal JwtClaim userClaim,
      Pageable pageable,
      @RequestParam(required = false) String keyword) {
    return ApiResponse.ok(orderService.getMyOrder(pageable, userClaim.getUserId(), keyword));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
  @PatchMapping("/{orderId}/{orderState}")
  public ApiResponse<Long> updateOrderState(@AuthenticationPrincipal JwtClaim userClaim,
      @PathVariable(name = "orderId") Long orderId,
      @PathVariable(name = "orderState") String orderState) {
    return ApiResponse.ok(
        orderService.updateOrderState(userClaim.getUserId(), orderId, orderState));
  }


}
