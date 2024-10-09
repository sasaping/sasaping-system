package com.sparta.promotion.server.domain.model;

import com.sparta.promotion.server.domain.model.vo.CouponType;
import com.sparta.promotion.server.domain.model.vo.DiscountType;
import com.sparta.promotion.server.presentation.request.CouponRequest;
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
  private Integer quantity;

  @Column(nullable = false)
  private Timestamp startDate;

  @Column(nullable = false)
  private Timestamp endDate;

  @Column(length = 15)
  private String userTier;

  private Long eventId;

  public static Coupon create(CouponRequest.Create request) {
    return Coupon.builder()
        .name(request.getName())
        .type(request.getType())
        .discountType(request.getDiscountType())
        .discountValue(request.getDiscountValue())
        .minBuyPrice(request.getMinBuyPrice())
        .maxDiscountPrice(request.getMaxDiscountPrice())
        .quantity(request.getQuantity())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .userTier(request.getUserTier())
        .eventId(request.getEventId())
        .build();
  }

  public void updateQuantity(Integer quantity) {
    this.quantity = quantity;
  }

}
