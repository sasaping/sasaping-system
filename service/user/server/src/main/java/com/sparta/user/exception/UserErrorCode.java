package com.sparta.user.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

  USER_CONFLICT(CONFLICT, "이미 존재하는 사용자입니다.");

  private final HttpStatus status;
  private final String message;
}
