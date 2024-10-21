package com.sparta.promotion.server.infrastructure.messaging;

import com.sparta.common.domain.entity.KafkaTopicConstant;
import com.sparta.promotion.server.application.service.CouponService;
import com.sparta.promotion.server.application.service.DistributedLockComponent;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponEventConsumer {

  private final CouponService couponService;
  private final DistributedLockComponent lockComponent;

  @KafkaListener(topics = KafkaTopicConstant.PROVIDE_EVENT_COUPON, groupId = "coupon-service-group")
  public void handleCouponIssue(CouponRequest.Event couponEvent) {
    Long userId = couponEvent.getUserId();
    Long couponId = couponEvent.getCouponId();

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
