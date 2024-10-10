package com.sparta.user.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PointHistoryType {
  EARN("적립"),
  USE("사용"),
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
        .orElseThrow(() -> new UserException(UserErrorCode.INVALID_POINT_HISTORY_TYPE));
  }

}
