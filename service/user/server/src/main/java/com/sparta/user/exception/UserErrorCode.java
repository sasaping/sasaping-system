package com.sparta.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  USER_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
  INVAILD_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  INVALID_POINT_HISTORY_TYPE(HttpStatus.NOT_FOUND, "포인트 타입을 찾을 수 없습니다."),
  INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),

  TIER_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 등급입니다."),
  TIER_NOT_FOUND(HttpStatus.NOT_FOUND, "등급을 찾을 수 없습니다."),

  ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송지를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}
