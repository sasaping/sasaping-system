package com.sparta.order.server.Cart.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.order.server.Cart.application.service.CartService;
import com.sparta.order.server.Cart.presentation.request.CartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping("/products")
  public ApiResponse<?> addCart(@RequestBody CartRequest.Add request) {
    cartService.addCart(request);
    return ApiResponse.created(null);
  }

}
