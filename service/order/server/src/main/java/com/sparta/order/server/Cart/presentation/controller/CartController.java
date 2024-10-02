package com.sparta.order.server.Cart.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.Cart.application.service.CartService;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.AddRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/products")
  public ApiResponse<?> addCart(@RequestBody AddRequest request) {
    cartService.addCart(request);
    return ApiResponse.created(null);
  }

  @GetMapping
  public ApiResponse<Map<String, CartDto.ProductInfoDto>> getCartList(
      @RequestParam(name = "userId") Long userId) {
    return ApiResponse.ok(cartService.updateCart(userId));
  }

}
