package com.sparta.promotion.server.domain.model;

import com.sparta.promotion.server.domain.model.vo.CouponType;
import com.sparta.promotion.server.domain.model.vo.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_coupon")
@Entity
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coupon_id")
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 15)
  private CouponType type;

  @Column(nullable = false, length = 25)
  private DiscountType discountType;

  @Column(nullable = false)
  private BigDecimal discountValue;

  private BigDecimal minBuyPrice;

  private BigDecimal maxDiscountPrice;

  @Column(nullable = false)
  private Timestamp startDate;

  @Column(nullable = false)
  private Timestamp endDate;

  @Column(length = 15)
  private String userTier;

  private Long eventId;

}
