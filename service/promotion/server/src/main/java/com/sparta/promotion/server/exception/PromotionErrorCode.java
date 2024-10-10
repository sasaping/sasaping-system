package com.sparta.promotion.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PromotionErrorCode {
  INVALID_COUPON_TYPE(HttpStatus.NOT_FOUND, "해당 쿠폰 종류가 없습니다."),
  INVALID_DISCOUNT_TYPE(HttpStatus.NOT_FOUND, "해당 할인 타입이 없습니다."),
  COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."),
  INSUFFICIENT_COUPON(HttpStatus.BAD_REQUEST, "쿠폰 수량이 부족합니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류");

  private final HttpStatus status;
  private final String message;
}
