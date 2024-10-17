package com.sparta.promotion.server.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.promotion.server.application.service.CouponService;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import com.sparta.promotion.server.presentation.response.CouponResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class CouponController {

  private final CouponService couponService;

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @PostMapping("/event")
  public ApiResponse<?> createEventCoupon(@RequestBody @Valid CouponRequest.Create request) {
    couponService.createEventCoupon(request);
    return ApiResponse.ok();
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
  @PostMapping("/event/{couponId}")
  public ApiResponse<?> provideEventCoupon(
      @PathVariable(name = "couponId") Long couponId,
      @AuthenticationPrincipal JwtClaim claim
  ) {
    couponService.provideEventCoupon(claim.getUserId(), couponId);
    return ApiResponse.ok();
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping
  public ApiResponse<Page<CouponResponse.Get>> getCouponList(Pageable pageable) {
    return ApiResponse.ok(couponService.getCouponList(pageable));
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping("/{couponId}")
  public ApiResponse<CouponResponse.Get> getCoupon(@PathVariable(name = "couponId") Long couponId) {
    return ApiResponse.ok(couponService.getCoupon(couponId));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping("/users/{userId}")
  public ApiResponse<Page<CouponResponse.Get>> getCouponListByUserId(
      @PathVariable(name = "userId") Long userId, Pageable pageable) {
    return ApiResponse.ok(couponService.getCouponListBoyUserId(userId, pageable));
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ApiResponse<Page<CouponResponse.Get>> getCouponListByUser(
      @AuthenticationPrincipal JwtClaim jwtClaim, Pageable pageable) {
    return ApiResponse.ok(couponService.getCouponListBoyUserId(jwtClaim.getUserId(), pageable));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @PatchMapping("/{couponId}")
  public ApiResponse<?> updateCoupon(
      @PathVariable(name = "couponId") Long couponId,
      @RequestBody @Valid CouponRequest.Update request) {
    couponService.updateCoupon(couponId, request);
    return ApiResponse.ok();
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @DeleteMapping("/{couponId}")
  public ApiResponse<?> deleteCoupon(@PathVariable(name = "couponId") Long couponId) {
    couponService.deleteCoupon(couponId);
    return ApiResponse.ok();
  }

}
