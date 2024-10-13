package com.sparta.user.application.dto;

import com.sparta.user.domain.model.User;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponse {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Info {

    private Long userId;
    private String username;
    private String role;
    private BigDecimal point;

    public static Info of(User user) {
      return Info.builder()
          .userId(user.getId())
          .username(user.getUsername())
          .role(user.getRole().name())
          .point(user.getPoint())
          .build();
    }

  }

}
