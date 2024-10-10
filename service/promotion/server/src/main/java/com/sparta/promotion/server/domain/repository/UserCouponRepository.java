package com.sparta.promotion.server.domain.repository;

import com.sparta.promotion.server.domain.model.UserCoupon;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository {

  UserCoupon save(UserCoupon userCoupon);

}
