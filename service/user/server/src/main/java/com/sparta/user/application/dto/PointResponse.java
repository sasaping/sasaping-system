package com.sparta.user.application.dto;

import com.sparta.user.domain.model.PointHistory;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointResponse {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Get {

    private Long pointHistoryId;
    private Long userId;
    private Long orderId;
    private BigDecimal point;
    private String type;

    public static PointResponse.Get of(PointHistory pointHistory) {
      return PointResponse.Get.builder()
          .pointHistoryId(pointHistory.getId())
          .userId(pointHistory.getUser().getId())
          .orderId(pointHistory.getOrderId())
          .point(pointHistory.getPoint())
          .type(pointHistory.getType().name())
          .build();
    }

  }

}
