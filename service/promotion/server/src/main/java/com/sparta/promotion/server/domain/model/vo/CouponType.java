package com.sparta.promotion.server.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.promotion.server.exception.PromotionErrorCode;
import com.sparta.promotion.server.exception.PromotionException;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CouponType {
  TIER("TIER"),
  EVENT("EVENT");

  private String type;

  @JsonValue
  public String getType() {
    return this.type;
  }

  @JsonCreator
  public static CouponType from(String type) {
    return Arrays.stream(CouponType.values())
        .filter(t -> t.getType().equals(type))
        .findFirst()
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.INVALID_COUPON_TYPE));
  }
}
