package com.sparta.auth.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignIn {

    private String token;

    public static SignIn of(String token) {
      return SignIn.builder().token(token).build();
    }
  }
}
