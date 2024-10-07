package com.sparta.order.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode {
  INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
  ORDER_NO_GENERATION_FAILED(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "주문 번호 생성에 실패했습니다. 잠시 후 다시 시도해주세요.");

  private final HttpStatus status;
  private final String message;

  OrderErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
