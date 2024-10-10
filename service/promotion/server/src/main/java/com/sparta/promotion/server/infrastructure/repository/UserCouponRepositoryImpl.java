package com.sparta.promotion.server.infrastructure.repository;

import com.sparta.promotion.server.domain.model.UserCoupon;
import com.sparta.promotion.server.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

  private final JpaUserCouponRepository jpaUserCouponRepository;
  
  @Override
  public UserCoupon save(UserCoupon userCoupon) {
    return jpaUserCouponRepository.save(userCoupon);
  }

}
