package com.sparta.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TierRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    private String name;
    private Long thresholdPrice;

  }

}
