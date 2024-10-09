package com.sparta.promotion.server.application.service;

import com.sparta.promotion.server.domain.model.Coupon;
import com.sparta.promotion.server.domain.repository.CouponRepository;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CouponService {

  private final CouponRepository couponRepository;

  // TODO(경민): 이벤트 테이블을 만들어서 이벤트 ID가 들어올 경우, 해당 이벤트가 있는지 검사해야함.
  @Transactional
  public void createEventCoupon(CouponRequest.Create request) {
    /* 이벤트 검사 코드
    if (request.getEventId() == null) {
      throw new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND);
    }
    eventRepository
        .findById(request.getEventId())
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND));
    */
    couponRepository.save(Coupon.create(request));
  }

}
