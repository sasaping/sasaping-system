package com.sparta.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

  USER_CONFLICT("409", "이미 존재하는 사용자입니다.");

  private final String status;
  private final String message;
}
