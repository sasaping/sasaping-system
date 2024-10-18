package com.sparta.promotion.server.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponLockService {

  private final CouponService couponService;
  private final DistributedLockComponent lockComponent;

  // TODO(경민): 유저 아이디로 해당 유저가 존재하는지 확인해야 함
  public void provideEventCoupon(Long userId, Long couponId) {
    lockComponent.execute(
        "couponProvideLock_%s".formatted(couponId),
        3000,
        3000,
        () -> {
          couponService.provideEventCouponInternal(userId, couponId);
        }
    );
  }


}
