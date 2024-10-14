package com.sparta.promotion.server.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_user_coupon")
@Entity
public class UserCoupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_coupon_id")
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long couponId;

  @Column(nullable = false)
  private Boolean isUsed;

  public static UserCoupon create(Long userId, Long couponId) {
    return UserCoupon.builder()
        .userId(userId)
        .couponId(couponId)
        .isUsed(false)
        .build();
  }

}
