package com.sparta.user.dto.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserInternalDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Get {

    private Long userId;
    private String username;
    private String password;
    private String role;

  }

}
