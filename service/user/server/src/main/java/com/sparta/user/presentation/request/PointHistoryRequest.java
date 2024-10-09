package com.sparta.user.presentation.request;

import com.sparta.user.domain.model.vo.PointHistoryType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointHistoryRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @NotNull
    private Long userId;
    private Long orderId;
    @NotNull
    private BigDecimal point;
    @NotNull
    private PointHistoryType type;
    @NotNull
    private String description;

  }

}
