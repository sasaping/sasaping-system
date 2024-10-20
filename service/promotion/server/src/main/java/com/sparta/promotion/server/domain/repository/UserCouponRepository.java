package com.sparta.promotion.server.domain.repository;

import com.sparta.promotion.server.domain.model.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository {

  UserCoupon save(UserCoupon userCoupon);

  Page<UserCoupon> findByUserId(Long userId, Pageable pageable);

  UserCoupon findByUserIdAndCouponId(Long userId, Long couponId);

}
