package com.sparta.promotion.server.infrastructure.repository;

import com.sparta.promotion.server.domain.model.Coupon;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
  Optional<Coupon> findByIdWithPessimisticLock(@Param("couponId") Long couponId);

}
