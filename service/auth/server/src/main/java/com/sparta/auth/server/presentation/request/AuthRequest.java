package com.sparta.auth.server.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignIn {

    private String username;
    private String password;

  }

}
