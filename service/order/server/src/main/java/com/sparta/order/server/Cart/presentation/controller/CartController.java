package com.sparta.order.server.Cart.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.Cart.application.service.CartService;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  // TODO userId 인증 객체로 변경 필요

  @GetMapping
  public ApiResponse<Map<String, CartDto.ProductInfoDto>> getCartList(
          @RequestParam(name = "userId") Long userId) {
    return ApiResponse.ok(cartService.getCart(userId));
  }

  @PostMapping("/products")
  public ApiResponse<?> addCart(@RequestBody CartDto.AddRequest request) {
    cartService.addCart(request);
    return ApiResponse.created(null);
  }

  @PatchMapping("/products")
  public ApiResponse<?> updateCart(@RequestBody CartDto.UpdateRequest request) {
    cartService.updateCart(request);
    return ApiResponse.ok(null);
  }

}
