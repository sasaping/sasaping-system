package com.sparta.auth.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO(경민): 인증/인가 작업 시 gateway에서 필요해보임. auth.dto를 만들어서 보관할지 common에 보관할지 고민. 인증/인가 작업 때 결정 후 진행.
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtClaim {

  private Long userId;
  private String username;
  private String role;

  public static JwtClaim create(Long userId, String username, String role) {
    return new JwtClaim(userId, username, role);
  }

}
