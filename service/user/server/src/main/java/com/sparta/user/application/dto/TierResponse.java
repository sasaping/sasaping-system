package com.sparta.user.application.dto;

import com.sparta.user.domain.model.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TierResponse {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Get {

    private Long tierId;
    private String name;
    private Long thresholdPrice;

    public static Get of(Tier tier) {
      return Get.builder()
          .tierId(tier.getId())
          .name(tier.getName())
          .thresholdPrice(tier.getThresholdPrice())
          .build();
    }

  }

}
