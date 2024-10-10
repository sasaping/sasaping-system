package com.sparta.order.server.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.Cart.presentation.dto.CartDto;
import com.sparta.order.server.Cart.presentation.dto.CartDto.CartProductResponse;
import com.sparta.order.server.application.service.CartService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @GetMapping
  public ApiResponse<List<CartProductResponse>> getCartList(
      @AuthenticationPrincipal JwtClaim userClaim) {
    return ApiResponse.ok(cartService.getCart(userClaim.getUserId()));
  }

  @PostMapping("/products")
  public ApiResponse<?> addCart(@AuthenticationPrincipal JwtClaim userClaim,
      @RequestBody @Valid CartDto.CartProductRequest cartProductRequest) {
    cartService.addCart(userClaim.getUserId(), cartProductRequest);
    return ApiResponse.created(null);
  }

  @PatchMapping("/products")
  public ApiResponse<?> updateCart(@AuthenticationPrincipal JwtClaim userClaim,
      @RequestBody @Valid CartDto.CartProductRequest cartProductRequest) {
    cartService.updateCart(userClaim.getUserId(), cartProductRequest);
    return ApiResponse.ok(null);
  }

  @DeleteMapping("/products/{productId}")
  public ApiResponse<?> deleteCart(@AuthenticationPrincipal JwtClaim userClaim,
      @PathVariable(name = "productId") String productId
  ) {
    cartService.deleteCart(userClaim.getUserId(), productId);
    return ApiResponse.ok(null);
  }

  @DeleteMapping("/clear")
  public ApiResponse<?> clearCart(@AuthenticationPrincipal JwtClaim userClaim) {
    cartService.clearCart(userClaim.getUserId());
    return ApiResponse.ok(null);
  }

}
