package com.sparta.promotion.server.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.promotion.server.exception.PromotionErrorCode;
import com.sparta.promotion.server.exception.PromotionException;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DiscountType {
  PRICE("PRICE"),
  PERCENTAGE("PERCENTAGE");

  private String discountType;

  @JsonValue
  public String getDiscountType() {
    return this.discountType;
  }

  @JsonCreator
  public static DiscountType from(String type) {
    return Arrays.stream(DiscountType.values())
        .filter(t -> t.getDiscountType().equals(type))
        .findFirst()
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.INVALID_DISCOUNT_TYPE));
  }
}
