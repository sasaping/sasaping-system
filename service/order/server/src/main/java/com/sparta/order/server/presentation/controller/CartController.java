package com.sparta.order.server.Cart.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.Cart.application.service.CartService;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.CartProductResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  // TODO userId 인증 객체로 변경 필요

  @GetMapping
  public ApiResponse<List<CartProductResponse>> getCartList(
      @RequestParam(name = "userId") Long userId) {
    return ApiResponse.ok(cartService.getCart(userId));
  }

  @PostMapping("/products")
  public ApiResponse<?> addCart(
      @RequestBody @Valid CartDto.CartProductRequest cartProductRequest) {
    cartService.addCart(cartProductRequest);
    return ApiResponse.created(null);
  }

  @PatchMapping("/products")
  public ApiResponse<?> updateCart(
      @RequestBody @Valid CartDto.CartProductRequest cartProductRequest) {
    cartService.updateCart(cartProductRequest);
    return ApiResponse.ok(null);
  }

  @DeleteMapping("/products/{productId}")
  public ApiResponse<?> deleteCart(
      @PathVariable(name = "productId") String productId,
      @RequestParam(name = "userId") Long userId) {
    cartService.deleteCart(userId, productId);
    return ApiResponse.ok(null);
  }

  @DeleteMapping("/clear")
  public ApiResponse<?> clearCart(@RequestParam(name = "userId") Long userId) {
    cartService.clearCart(userId);
    return ApiResponse.ok(null);
  }

}
