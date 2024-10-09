package com.sparta.promotion.server.domain.repository;

import com.sparta.promotion.server.domain.model.Coupon;
import java.util.Optional;

public interface CouponRepository {

  Coupon save(Coupon coupon);

  Optional<Coupon> findById(Long couponId);

}
