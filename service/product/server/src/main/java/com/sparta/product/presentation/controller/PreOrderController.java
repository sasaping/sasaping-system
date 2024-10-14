package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.preorder.PreOrderService;
import com.sparta.product.domain.model.PreOrderState;
import com.sparta.product.presentation.request.PreOrderCreateRequest;
import com.sparta.product.presentation.request.PreOrderUpdateRequest;
import com.sparta.product.presentation.response.PreOrderResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preorders")
@RequiredArgsConstructor
@Validated
public class PreOrderController {
  private final PreOrderService preOrderService;

  @PostMapping
  public ApiResponse<Long> createPreOrder(@RequestBody @Valid PreOrderCreateRequest request) {
    return ApiResponse.created(preOrderService.createPreOrder(request));
  }

  @PatchMapping
  public ApiResponse<PreOrderResponse> updatePreOrder(
      @RequestBody @Valid PreOrderUpdateRequest request) {
    return ApiResponse.ok(preOrderService.updatePreOrder(request));
  }

  @DeleteMapping("/{preOrderId}")
  public ApiResponse<Void> deletePreOrder(@NotNull @PathVariable("preOrderId") Long preOrderId) {
    preOrderService.deletePreOrder(preOrderId);
    return ApiResponse.ok();
  }

  @PatchMapping("/{preOrderId}/open")
  public ApiResponse<PreOrderResponse> openPreOrder(
      @NotNull @PathVariable("preOrderId") Long preOrderId) {
    return ApiResponse.ok(preOrderService.updateState(preOrderId, PreOrderState.OPEN_FOR_ORDER));
  }

  @PatchMapping("/{preOrderId}/cancel")
  public ApiResponse<PreOrderResponse> cancelPreOrder(
      @NotNull @PathVariable("preOrderId") Long preOrderId) {
    return ApiResponse.ok(preOrderService.updateState(preOrderId, PreOrderState.CANCELLED));
  }

  @GetMapping("/{preOrderId}")
  public ApiResponse<PreOrderResponse> getPreOrder(
      @NotNull @PathVariable("preOrderId") Long preOrderId) {
    return ApiResponse.ok(preOrderService.getPreOrder(preOrderId));
  }

  @GetMapping
  public ApiResponse<Page<PreOrderResponse>> getPreOrderList(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "30") @Min(1) int size) {
    return ApiResponse.ok(preOrderService.getPreOrderList(page, size));
  }
}
