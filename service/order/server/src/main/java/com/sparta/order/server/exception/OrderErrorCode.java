package com.sparta.order.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode {
  INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.");

  private final HttpStatus status;
  private final String message;

  OrderErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
