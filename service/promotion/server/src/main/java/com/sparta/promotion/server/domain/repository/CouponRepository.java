package com.sparta.promotion.server.domain.repository;

import com.sparta.promotion.server.domain.model.Coupon;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepository {

  Coupon save(Coupon coupon);

  Optional<Coupon> findById(Long couponId);

  Optional<Coupon> findByIdWithPessimisticLock(Long couponId);

  Page<Coupon> findAll(Pageable pageable);

}
