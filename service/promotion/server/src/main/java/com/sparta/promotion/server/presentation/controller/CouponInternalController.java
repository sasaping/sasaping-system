package com.sparta.promotion.server.presentation.controller;

import com.sparta.promotion.server.application.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/internal/coupons")
@RestController
public class CouponInternalController {

  private final CouponService couponService;

  @PatchMapping("/{couponId}/use")
  public void useCoupon(
      @PathVariable(name = "couponId") Long couponId,
      @RequestParam(name = "userId") Long userId) {
    couponService.useCoupon(couponId, userId);
  }

  @PatchMapping("/{couponId}/refund")
  public void refundCoupon(
      @PathVariable(name = "couponId") Long couponId,
      @RequestParam(name = "userId") Long userId) {
    couponService.refundCoupon(couponId, userId);
  }

}
