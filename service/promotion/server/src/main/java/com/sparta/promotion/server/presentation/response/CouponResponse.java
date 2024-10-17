package com.sparta.promotion.server.presentation.response;

import com.sparta.promotion.server.domain.model.Coupon;
import com.sparta.promotion.server.domain.model.vo.CouponType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CouponResponse {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Get {

    private Long couponId;
    private String name;
    private CouponType type;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minBuyPrice;
    private BigDecimal maxDiscountPrice;
    private Integer quantity;
    private Timestamp startDate;
    private Timestamp endDate;
    private String userTier;
    private Long eventId;

    public static Get of(Coupon coupon) {
      return Get.builder()
          .couponId(coupon.getId())
          .name(coupon.getName())
          .type(coupon.getType())
          .discountType(coupon.getDiscountType().name())
          .discountValue(coupon.getDiscountValue())
          .minBuyPrice(coupon.getMinBuyPrice())
          .maxDiscountPrice(coupon.getMaxDiscountPrice())
          .quantity(coupon.getQuantity())
          .startDate(coupon.getStartDate())
          .endDate(coupon.getEndDate())
          .userTier(coupon.getUserTier())
          .eventId(coupon.getEventId())
          .build();
    }

  }

}
