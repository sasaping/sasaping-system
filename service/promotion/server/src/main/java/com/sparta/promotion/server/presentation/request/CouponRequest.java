package com.sparta.promotion.server.presentation.request;

import com.sparta.promotion.server.domain.model.vo.CouponType;
import com.sparta.promotion.server.domain.model.vo.DiscountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CouponRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private CouponType type;

    @NotNull
    private DiscountType discountType;

    @NotNull
    private BigDecimal discountValue;

    private BigDecimal minBuyPrice;

    private BigDecimal maxDiscountPrice;

    @NotNull
    private Integer quantity;

    @NotNull
    private Timestamp startDate;

    @NotNull
    private Timestamp endDate;

    @Size(max = 15)
    private String userTier;

    private Long eventId;

  }

}
