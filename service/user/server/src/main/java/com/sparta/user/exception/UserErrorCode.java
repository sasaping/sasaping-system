package com.sparta.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  USER_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),

  TIER_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 등급입니다.");

  private final HttpStatus status;
  private final String message;
}
