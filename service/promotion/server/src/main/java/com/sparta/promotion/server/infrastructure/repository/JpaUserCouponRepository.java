package com.sparta.promotion.server.infrastructure.repository;

import com.sparta.promotion.server.domain.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Long> {

}
