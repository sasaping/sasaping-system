package com.sparta.user.user_dto.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long userId;
  private String username;
  private String password;
  private String role;
}
