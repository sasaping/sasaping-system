package com.sparta.promotion.server.infrastructure.repository;

import com.sparta.promotion.server.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

}
