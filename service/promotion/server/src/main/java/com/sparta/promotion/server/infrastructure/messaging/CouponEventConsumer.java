package com.sparta.promotion.server.infrastructure.messaging;

import com.sparta.common.domain.entity.KafkaTopicConstant;
import com.sparta.promotion.server.application.service.CouponService;
import com.sparta.promotion.server.application.service.DistributedLockComponent;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CouponEventConsumer")
@RequiredArgsConstructor
@Service
public class CouponEventConsumer {

  private final CouponService couponService;
  private final DistributedLockComponent lockComponent;

  @KafkaListener(topics = KafkaTopicConstant.PROVIDE_EVENT_COUPON, groupId = "coupon-service-group")
  public void handleCouponIssue(CouponRequest.Event couponEvent) {
    Long userId = couponEvent.getUserId();
    Long couponId = couponEvent.getCouponId();

    log.info("provide coupon userId: {}", userId);
    lockComponent.execute(
        "couponProvideLock_%s".formatted(couponId),
        3000,
        3000,
        () -> {
          couponService.provideEventCouponInternal(userId, couponId);
        }
    );
    log.info("provide coupon couponId: {}", couponId);
  }

}
