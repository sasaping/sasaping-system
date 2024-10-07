package com.sparta.user.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PointHistoryType {
  EARN("적립"),
  REDEEM("사용"),
  REFUND("환불");

  private final String type;

  @JsonValue
  public String getType() {
    return this.type;
  }

  @JsonCreator
  public static PointHistoryType from(String type) {
    return Arrays.stream(PointHistoryType.values())
        .filter(t -> t.getType().equals(type))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
  }

}
