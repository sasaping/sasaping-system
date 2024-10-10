package com.sparta.user.user_dto.infrastructure;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryDto {

  private Long userId;
  private Long orderId;
  private BigDecimal point;
  private String type;
  private String description;

}
