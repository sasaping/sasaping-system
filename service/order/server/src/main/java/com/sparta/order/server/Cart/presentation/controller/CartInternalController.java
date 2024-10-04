package com.sparta.order.server.Cart.presentation.controller;

import com.sparta.order.server.Cart.application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/carts")
public class CartInternalController {

  private final CartService cartService;

  @PatchMapping("/products/info-update/{productId}")
  public void updateCartInfo(@PathVariable(name = "productId") String productId) {
    cartService.updateCartInfo(productId);
  }

}
